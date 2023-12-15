dependencies {
    compileOnly("dev.hollowcube:minestom-ce-snapshots:1_20_2-e7f833b499")
    compileOnly("dev.hollowcube:minestom-ce-extensions:1.2.0")
    api(project(":prefixes-api"))
    api(project(":prefixes-shared"))
    compileOnly(files("libs/lp-minestom-5.4-SNAPSHOT-all.jar"))
}