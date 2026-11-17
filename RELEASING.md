# Preparing a Maven Central release

The release workflow builds a signed Maven Central bundle; it does not upload or publish it. The current release candidate is `2.0.0`.

1. Merge the release preparation PR and verify the build matrix on the release commit.
2. Ensure the `io.github.mikheevshow` namespace is available to the publisher account in the [Central Publisher Portal](https://central.sonatype.com/publishing/namespaces), and that the release signing public key is published to a public key server.
3. Configure GitHub Actions secrets `SIGNING_KEY` (ASCII-armored private key), `SIGNING_PASSWORD` (key passphrase), and `SIGNING_KEY_ID` (the key ID expected by Gradle's signing plugin). Keep these credentials out of the repository.
4. Run **Prepare release bundle** from GitHub Actions for the intended release commit. Download the `central-bundle` artifact and inspect its contents and signatures. The archive can also be built locally using `./gradlew clean build centralBundle` with the same signing values supplied as environment variables.
5. In the Central Publisher Portal, upload the ZIP from `build/central-bundle/` or the workflow artifact. Choose the user-managed process, wait for validation, review the deployment, and explicitly publish it. Portal validation requires a verified namespace and a publicly retrievable signing key. Published Maven Central versions cannot be replaced.
6. Once `io.github.mikheevshow:nmt-metrics-spring-boot-starter:2.0.0` is available from Maven Central, update the README's release-status note and create the GitHub release/tag from the verified commit.

The staging repository is recreated on each `centralBundle` build to avoid including stale artifacts. No Portal token is needed to build the archive; the Portal account is needed for upload and publication. See [Sonatype's bundle upload documentation](https://central.sonatype.org/publish/publish-portal-upload/) for archive and validation requirements.
