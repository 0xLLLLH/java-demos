java_binary(
    name = "Main",
    srcs = glob(["src/main/java/org/example/*.java"]),
    visibility = ["//src/test/java/org/example:__pkg__"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
    ],
)
