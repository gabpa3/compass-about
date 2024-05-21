# compass-about

![Kotlin](https://img.shields.io/badge/kotlin-1.9.24-blue.svg) ![Coroutines](https://img.shields.io/badge/coroutines-1.8.0-blueviolet) ![Room](https://img.shields.io/badge/room-2.61-yellow)  ![Room](https://img.shields.io/badge/okhttp-4.12.0-red) ![Room](https://img.shields.io/badge/junit-4.13.2-black)

## Description
This Android project aims to retrieve the content of a given webpage and process it using two different data manipulation requirements: finding every 10th character and finding the character sequence until a whitespace appears.

## Approach
The application concurrently fetches content from a specified webpage one time it gets then cached on a database and provides two parallel concurrence mechanisms to achieve the requirements. How its work?
  - Well-defined structure based on Clean Architecture(UI, Data, Domain) and MVVM pattern to notify view states. Usecase for business logic separation and repository for data sources
  - Okhttp for creating an HTTP client and making the request. In this particular scenario, Retrofit was not needed as a mechanism for retrieving data online
  - Room as cache mechanism. For instance, to save and provide the webpage content as a single plain text
  - Coroutines and Flows. One for grabbing the content meanwhile suspend functions and Dispatcher.IO to be a safe thread and the other for manipulating the content and emitting and collecting reactive streams enabling asynchronous data flow. In this last part use Dispatcher.Default
  - DS as LinkedHashMap, HashMap, and StringBuilder to better time performance
  - Service locator pattern as a mechanism to encapsulate dependencies and provide when they will be required
  - JUnit and Kotlin coroutine test to cover and ensure software quality. In this context, All Domain codes, repository, and viewmodel were coverage with 100%
  - UI split into two fragments for a welcome and show the lists
  - Configuration changes
  - Following git flow workflow

This approach meets the requirements, ensures a well-structured codebase, promotes separation of concerns, and enhances the maintainability and the code quality of the application

## Consideration
To achieve finding character sequence correctly. More than taking whitespace and break lines as delimiter I had to include '<' and '>' to match one of the requirements. So, every time to find an HTML tag glued to a word I split the sequence.

## Screenshots & videos

portrait|landscape
------|------
<video src="https://github.com/gabpa3/compass-about/assets/16760596/28da0083-aab0-4318-9155-ca97af750e1d"> | <video src="https://github.com/gabpa3/compass-about/assets/16760596/e38cb3e5-13a1-4004-90bf-5b8651a5bbeb" width="300">

### Coverage

<p>Data - Repository</p>
<img src="https://github.com/gabpa3/compass-about/assets/16760596/0bfb42c3-166a-4b2a-9a64-52ca34390a52" />

<p>Domain</p>
<img src="https://github.com/gabpa3/compass-about/assets/16760596/34de4252-7323-4744-9d16-bb784374abae" />

<p>ViewModel</p>
<img src="https://github.com/gabpa3/compass-about/assets/16760596/3e36a73b-0aa4-4d0f-887f-efb32015b88a" />


## References
Developer Android - Google -> https://developer.android.com/</br>
Sunflower project - Android -> https://github.com/android/sunflower
Kotlin Documentation - Jetbrains -> https://kotlinlang.org/docs/home.html

## License
```
Copyright 2024 Gabriel Perez

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
