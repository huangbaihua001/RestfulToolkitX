# Restful Toolkit For Intellij IDEA

![Build](https://github.com/huangbaihua001/RestfulToolkitX/workflows/Build/badge.svg)

## RestfulToolkitX 是什么?
RestfulToolkitX 是运行在Intellij IDEA 之上的一个非常棒的 RESTful 开发工具包。支持 Java 和 Kotlin 语言，Spring 生态(Spring Boot / Spring MVC),JAX-RS.
兼容 IDEA 2020.2及以上版本。

---
RestfulToolkitX 的由来： 

原项目 [RestfulToolkit][RestfulToolkit] 之前个人也在使用。 但目前的版本，不兼容 IDEA 最新版本。 也用过基于该作者的其它版本，不是很理想。
所以个人决定在 RestfulToolkit 基础之上维护并完善 RestfulToolkitX。

## 功能

- [x] 基于 [IntelliJ Platform Plugin Template][template] 构建, 支持 Java 8+, IDEA 2020.2+。
- [x] 快捷键搜索 URL 并直接定位至服务. ( use: Ctrl + \ or Ctrl + Alt + N ) 。
- [x] 导航窗显示 Restful 服务结构。
- [x] 简单的 HttpClient 工具。
- [x] 在请求方法上 生成并复制参数，URL。
- [x] 将 Java类 转成 JSON; 格式化 JSON 数据 <em>( Windows: Ctrl + Enter; Mac: Command + Enter ) 。
- [ ] 其它有用的功能，敬请期待... ^_^


快速搜索 URL 预览

![searchService.png](img/searchService.png)

Restful 服务窗口预览

![restServiceWindow.png](img/restServiceWindow.png)

## 安装

- IDE 插件安装:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>搜索 "
  RestfulToolkitX"</kbd> >
  <kbd>安装插件</kbd>

- 手动安装:

  下载最新版 [latest release](https://github.com/huangbaihua001/restful-toolkit/releases/latest) 然后手动安装，操作入口:
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>



## License

RestfulToolkitX is under the Apache 2.0 license. See the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) file for details.

---
Plugin based on the [IntelliJ Platform Plugin Template][template], [RestfulToolkit][RestfulToolkit]

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[RestfulToolkit]: https://github.com/mrmanzhaow/RestfulToolkit