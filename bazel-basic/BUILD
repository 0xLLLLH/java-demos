java_binary(
    name = "Main",
    srcs = ["src/main/java/org/example/Main.java"],
    # Maven artifacts deps syntax:
    # com.google.guava:guava:31.1-jre -> @maven//:com_google_guava_guava
    deps = [":Greeting", "@maven//:com_google_guava_guava"]
)

java_binary(
    name = "Greeting",
    srcs = ["src/main/java/org/example/Greeting.java"],
    # see https://bazel.build/concepts/visibility for syntax
    visibility = ["//src/main/java/org/example/cmdline:__pkg__"]
)