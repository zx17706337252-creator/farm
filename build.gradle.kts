// Root build file for FarmLife project
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
