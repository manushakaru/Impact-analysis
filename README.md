

<p align="right">
  <img align="left" src="https://github.com/manushakaru/Impact-analysis/blob/master/src/main/resources/META-INF/pluginIcon.svg" alt="plugin icon" />
<a href="https://github.com/manushakaru/new-tvf/blob/master/package.json">
    <img src="https://img.shields.io/badge/dependencies-up%20to%20date%20-brightgreen.svg" alt="dependencies" />
  </a>
<a href="https://github.com/manushakaru/Impact-analysis/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/manushakaru/Impact-analysis"></a>
  <a href="https://github.com/manushakaru/Impact-analysis/network"><img alt="GitHub forks" src="https://img.shields.io/github/forks/manushakaru/Impact-analysis"></a>
  <a href="https://github.com/manushakaru/Impact-analysis/stargazers"><img alt="GitHub stars" src="https://img.shields.io/github/stars/manushakaru/Impact-analysis"></a>
  
  <a href="https://github.com/manushakaru/Impact-analysis">
   <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/manushakaru/Impact-analysis.svg">
  </a>
  
   <a href="https://github.com/manushakaru">
<img alt="GitHub followers" src="https://img.shields.io/github/followers/manushakaru.svg?label=follow&style=social">
  </a>
  </p>


  <h1 align="center">Automated Impact Analysis of Changing Parts of a Large Codebase </h1>
  
---
## About 

<p align="justify" >
In professional software development and productive software development, most of the companies choose IntelliJ IDEA as the main IDE for java development. Also, more than 65% of Java developers use IntelliJ IDEA. Because Intellij IDEA provides developer-friendly features like code suggestions, error handling, searching, etc. 
</p>

<p align="justify" >
In this project, we were focusing on change impact analysis in a java project. Because we recognized that the effort they have given on the change impact analysis is less in IntelliJ IDEA. Therefore, we decided to develop a plugin for Intellij IDEA which can handle change impact analysis. 
</p>

<p align="justify" >
Our plugin provides changes between the last commit and local changes and developers can view the impacts of selected changes. When the developer needs to know the impact of a method in a class which is not changed during the developments, the developer can run change impact analysis on the opened class and identify the impact of the methods in that class.
</p>

<p align="justify" >
One major objective of the project is to reduce the time taken to identify the impact of a change. By using the depth controlling mechanism, developers can limit the scanning depth of the impact of a change. It will reduce the time and computational cost because it stops scanning without exceeding the depth. We have done prototype plugin design and implementation, identify callers and callees of a method. 
</p>


## Run

- Clone [this](https://github.com/manushakaru/Impact-analysis/) project
```
git clone https://github.com/manushakaru/Impact-analysis
```
- Open project using IntelliJ
- Open [Gradle Tool Window](https://www.jetbrains.com/help/idea/jetgradle-tool-window.html) - Access: ``` View | Tool Windows | Gradle ```
- Navigate to ``` Impact Analaysis -> Tasks -> intellij ```in Gradle Tool Window
- Run ``` runIde ``` under intellij 

## Build Plugin 

- Clone [this](https://github.com/manushakaru/Impact-analysis/) project
```
git clone https://github.com/manushakaru/Impact-analysis
```
- Open project using IntelliJ
- Open [Gradle Tool Window](https://www.jetbrains.com/help/idea/jetgradle-tool-window.html) - Access: ``` View | Tool Windows | Gradle ```
- Navigate to ``` Impact Analaysis -> Tasks -> intellij ```in Gradle Tool Window
- Run ``` buildPlugin ``` under intellij 
- You can find the ``` Impact Analysis-1.0.zip ``` under ``` Impact-analysis\build\distributions ``` folder 

## Install
 
- Follow the instructions in ``` Build Plugin ``` to build the plugin **or** download the [``` Impact Analaysis-1.0.zip ```](https://github.com/manushakaru/Impact-analysis/releases/tag/1.0.0)
- In the Settings/Preferences dialog  ``` Ctrl+Alt+S ``` , select Plugins.
- On the Plugins page, click The Settings button and then click Install Plugin from Disk.
- Select the plugin archive file from ``` Impact-analysis\build\distributions ``` folder **or** downloaded file and click OK.
- Click OK to apply the changes and restart the IDE if prompted.
- You can find the plugin at bottom right corner - Access: ``` View | Tool Windows | Impact Analysis ```


## Usage

### Impact analysis on current .java file 
  - Open any java project in IntelliJ
  - Open java file you need to analyze 
  - Open Impact analysis plugin window 
  - Select ``` Current ``` radio button in plugin window 
  - Set the ``` Depth ``` you need to analyze
  - Click ``` Run ``` to get all the methods from opened java file
  - Select method from ``` Methods ``` list and you can see the impacted methods in ``` Impact ``` list
  
### Impact analysis on Git changes
  - Select ``` Git ``` radio button in plugin window 
  - Click ``` Run ``` to get all the git changes 
  - Set the ``` Depth ``` you need to analyze
  - Select method from ``` Methods ``` list and you can see the impacted methods in ``` Impact ``` list
  
## Special Notations 
 ![class](https://github.com/manushakaru/Impact-analysis/blob/master/src/main/resources/drawables/class.png) - Class <br>
 ![method](https://github.com/manushakaru/Impact-analysis/blob/master/src/main/resources/drawables/method.png) - Method <br>
 ![up arrow](https://github.com/manushakaru/Impact-analysis/blob/master/src/main/resources/drawables/up.png) - Caller <br>
 ![down arrow](https://github.com/manushakaru/Impact-analysis/blob/master/src/main/resources/drawables/down.png) - Callee <br>

