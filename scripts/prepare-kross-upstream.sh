#!/usr/bin/env bash
set -euo pipefail

# This script runs on the trusted Kross branch. It prepares a candidate in the
# local checkout and never runs code from the fetched upstream release.
upstream=${UPSTREAM_URL:-https://github.com/partymola/android-bike-radar-overlay.git}
version_file=gradle/kross-version.properties
base_version=$(sed -nE 's/^ +versionName = "([0-9]+\.[0-9]+\.[0-9]+)"$/\1/p' app/build.gradle.kts)
android_code=$(sed -nE 's/^androidVersionCode=([0-9]+)$/\1/p' "$version_file")
[[ $base_version =~ ^[0-9]+\.[0-9]+\.[0-9]+$ && $android_code =~ ^[1-9][0-9]*$ ]]
(( android_code < 2100000000 ))

tags=$(git ls-remote --tags --refs "$upstream" 'refs/tags/v*')
latest_tag=$(awk '$2 ~ /^refs\/tags\/v[0-9]+\.[0-9]+\.[0-9]+$/ { sub(/^refs\/tags\//, "", $2); print $2 }' <<<"$tags" | sort -V | tail -n 1)
[[ -n $latest_tag ]] || { echo "No stable upstream tags found" >&2; exit 1; }
latest_version=${latest_tag#v}
newest=$(printf '%s\n' "$base_version" "$latest_version" | sort -V | tail -n 1)
if [[ $latest_version == "$base_version" || $latest_version != "$newest" ]]; then
    printf 'Brak nowszej wersji oryginału. Baza Krossa to `%s`, najnowszy tag to `%s`.\n' \
        "$base_version" "$latest_tag" | tee -a "$GITHUB_STEP_SUMMARY"
    if [[ ${TEST_CURRENT:-false} == true ]]; then
        printf 'test_ref=%s\n' "$(git rev-parse HEAD)" >> "$GITHUB_OUTPUT"
    fi
    exit 0
fi

branch="automation/upstream-$latest_tag"
printf 'branch=%s\ntag=%s\n' "$branch" "$latest_tag" >> "$GITHUB_OUTPUT"
existing=$(git ls-remote --heads origin "refs/heads/$branch")
if [[ -n $existing ]]; then
    printf 'Gałąź `%s` już istnieje.\n' "$branch" >> "$GITHUB_STEP_SUMMARY"
    if [[ ${TEST_CURRENT:-false} == true ]]; then
        printf 'test_ref=%s\n' "${existing%%$'\t'*}" >> "$GITHUB_OUTPUT"
    fi
    exit 0
fi

expected=$(awk -v ref="refs/tags/$latest_tag" '$2 == ref { print $1 }' <<<"$tags")
git fetch --no-tags "$upstream" "refs/tags/$latest_tag"
[[ $(git rev-parse FETCH_HEAD) == "$expected" ]] || { echo "Upstream tag moved during fetch" >&2; exit 1; }
git switch -c "$branch"
if ! git -c user.name='github-actions[bot]' -c user.email='41898282+github-actions[bot]@users.noreply.github.com' -c rerere.enabled=false merge --no-ff --no-commit FETCH_HEAD; then
    {
        printf 'Aktualizacja do `%s` wymaga rozwiązania konfliktów.\n\n' "$latest_tag"
        git diff --name-only --diff-filter=U | sed 's/^/- /'
    } >> "$GITHUB_STEP_SUMMARY"
    git merge --abort
    exit 1
fi

merged_version=$(sed -nE 's/^ +versionName = "([0-9]+\.[0-9]+\.[0-9]+)"$/\1/p' app/build.gradle.kts)
if [[ $merged_version != "$latest_version" ]]; then
    printf 'Tag `%s` i wersja w Gradle `%s` różnią się.\n' "$latest_tag" "$merged_version" >> "$GITHUB_STEP_SUMMARY"
    exit 1
fi

printf 'revision=1\nandroidVersionCode=%s\n' "$((android_code + 1))" > "$version_file"
git add -- "$version_file"
git -c user.name='github-actions[bot]' -c user.email='41898282+github-actions[bot]@users.noreply.github.com' \
    commit -m "Prepare BikeRadar-Kross $latest_version.k1"
printf 'prepared=true\n' >> "$GITHUB_OUTPUT"
