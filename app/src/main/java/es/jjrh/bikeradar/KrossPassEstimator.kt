// SPDX-License-Identifier: GPL-3.0-or-later
package es.jjrh.bikeradar

/**
 * Estimates a pass from each approaching slot that disappears at close range.
 * The product exposes slots rather than durable target IDs.
 */
internal class KrossPassEstimator {
    private class Approach(val startM: Int, var lastM: Int, var lastSeenMs: Long, var samples: Int = 1)

    private val approaches = mutableMapOf<Int, Approach>()
    private var lastFrameMs: Long? = null

    fun reset() {
        approaches.clear()
        lastFrameMs = null
    }

    fun observe(vehicles: List<Vehicle>, nowMs: Long): Int {
        val previous = lastFrameMs
        if (previous != null && nowMs - previous !in 0..1_200L) reset()
        lastFrameMs = nowMs
        var passes = 0
        val targets = vehicles.associateBy { it.id }
        val entries = approaches.entries.iterator()
        while (entries.hasNext()) {
            val (id, approach) = entries.next()
            if (id in targets || nowMs - approach.lastSeenMs < 350L) continue
            if (approach.samples >= 4 && approach.lastM <= 10 && approach.startM - approach.lastM >= 9) passes++
            entries.remove()
        }
        for ((id, target) in targets) {
            val distance = target.distanceM
            val approach = approaches[id]
            if (approach == null || distance > approach.lastM + 4 || nowMs - approach.lastSeenMs >= 350L) {
                approaches[id] = Approach(distance, distance, nowMs)
            } else {
                approach.lastM = distance
                approach.lastSeenMs = nowMs
                approach.samples++
            }
        }
        return passes
    }
}
