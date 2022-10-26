# Bazel Basic

## 术语

可能涉及的术语：

- **工作区**：由根目录的`WORKSPACE`标识，必须设置工作区才能构建。
- **软件包**：由`BUILD`文件标识，一个`BUILD`文件对应一个软件包。同一工作区内可能存在多个软件包并可能有依赖关系。
- **构建规则**：例如`java_binary`规则
- **目标**：分为规则目标、文件目标和软件包组目标，目标可以被其他构建规则引用。详见[文档](https://bazel.build/reference/glossary#target)。
- **标签**：[标签](https://bazel.build/reference/glossary#label)用于引用目标

其他术语可查阅官方文档的[术语表](https://bazel.build/reference/glossary)

## 依赖管理

### 依赖同一软件包的规则目标
```
java_binary(
    name = "Main",
    # src 是源文件相对路径。相对于`BUILD`文件
    srcs = ["src/main/java/org/example/Main.java"],
    deps = [":Greeting"]
)

java_binary(
    name = "Greeting",
    srcs = ["src/main/java/org/example/Greeting.java"],
)
```

### 依赖不同软件包的规则目标
与依赖统一软件包的规则类似，主要是需要设置`visibility`来修改可见性

例如现在有个新软件包，声明了一个名为`Runner`的规则目标，依赖于`Greeting`
```
# src/main/java/org/example/cmdline/BUILD
java_binary(
    name = "Runner",
    srcs = ["Runner.java"],
    deps = ["//:Greeting"]
)
```

直接构建`//src/main/java/org/example/cmdline:Runner`将会报错，需要修改被依赖目标的[可见性](https://bazel.build/concepts/visibility)：
```
java_binary(
    name = "Greeting",
    srcs = ["src/main/java/org/example/Greeting.java"],
    # see https://bazel.build/concepts/visibility for syntax
    visibility = ["//src/main/java/org/example/cmdline:__pkg__"]
)
```

### 依赖Maven artifacts
需要在`WORKSPACE`中使用[rules_jvm_external](https://github.com/bazelbuild/rules_jvm_external)

以`guava`为例，`WORKSPACE`的`maven_install`可以参考`pom`来写：
```
# 前面照抄github即可

maven_install(
    artifacts = [
        # 格式 "some.group:artifactsId:version"
        "com.google.guava:guava:31.1-jre",
    ],
    repositories = [
        # 除中央仓库外，还可以指定私有仓库、国内镜像等
        "https://repo1.maven.org/maven2",
    ],
)
```

然后在`BUILD`文件中声明依赖：
```
java_binary(
    name = "Main",
    srcs = ["src/main/java/org/example/Main.java"],
    # Maven artifacts deps syntax:
    # com.google.guava:guava:31.1-jre -> @maven//:com_google_guava_guava
    deps = ["@maven//:com_google_guava_guava"]
)
```

最后点击idea右上角的Bazel图标触发同步，拉取依赖。
> Tips: 也可以用快捷键`option + Y`，不过要求光标不能在编辑器中

## 构建

### 手动构建

可以给`bazel build`命令传入规则目标的标签来进行指定目标的构建。

```
# 工作区根目录下的包
bazel build :Main
# 非根目录下的包
bazel build //src/main/java/org/example/cmdline:Runner
```

### 使用IDEA的Bazel插件自动构建

IDEA的Bazel插件用于拉取远程依赖，并在代码变化时自动构建所有软件包。

需要注意，插件并不是检测到`WORKSPACE`就会启动，而是需要使用「files -> Import Bazel Project」导入工作区。
这样打开的项目是启动了插件的。

具体操作详见[插件文档](https://ij.bazel.build/docs/import-project.html)。

## 执行

与`bazel build`类似，通过标签指定规则目标。

```
# 工作区根目录下的包
bazel run :Main
# 非根目录下的包
bazel run //src/main/java/org/example/cmdline:Runner
```
