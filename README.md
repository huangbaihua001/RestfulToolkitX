# Restful Toolkit For IntelliJ IDEA

![Build](https://github.com/huangbaihua001/RestfulToolkitX/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/jiux.net.plugin.restful.toolkit.svg)](https://plugins.jetbrains.com/plugin/18118-restfultoolkitx)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/jiux.net.plugin.restful.toolkit.svg)](https://plugins.jetbrains.com/plugin/18118-restfultoolkitx)

[中文](README_zh_CN.md)

![logo.png](img/logo.png)

## What is RestfulToolkitX?
RestfulToolkitX is an awesome restful development toolkit for Intellij IDEA. This toolkit supports Java and Kotlin languages,
Spring framework (Spring Boot / Spring MVC),JAX-RS.

## Features

- [x] Based on [IntelliJ Platform Plugin Template][template], support for Java 8+, IDEA 2020.2+.
- [x] Quick search url to navigate to service declaration. ( use: Ctrl + \ or Ctrl + Alt + N ) .
- [x] Show Restful services structure.
- [x] A simple http request tool.
- [x] Generate&Copy Query Param, Generate&Copy URL on the request method.
- [x] Convert java class to JSON; format json data <em>( Windows: Ctrl + Enter; Mac: Command + Enter ) .
- [ ] In development: Quick code generation from tables, including DAO (MyBatis, JPA), Entity (DTO), Service, Controller.


Quick Search URL Preview

![searchService.png](img/searchService.png)

Restful Services Window Preview

![restServiceWindow.png](img/rest_resp_highlight.png)

Useful Functions Preview

![gen_copy.png](img/gen_copy.png)
![convert_json.png](img/convert_json.png)


## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  RestfulToolkitX"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/huangbaihua001/restful-toolkit/releases/latest) and install it
  manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>



## License

RestfulToolkitX is under the Apache 2.0 license. See the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) file for details.

---
Plugin based on the [IntelliJ Platform Plugin Template][template], [RestfulToolkit][RestfulToolkit]

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[RestfulToolkit]: https://github.com/mrmanzhaow/RestfulToolkit
