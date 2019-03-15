# andytools

将release/refactor.jar放到要处理的目录

terminal中进到对应目录中，然后运行下面指令

测试效果，可以查看当前目录下 out/changelog.txt,预览效果
```
java -classpath refactor.jar RefactorKt
```

实际执行
```
java -classpath refactor.jar RefactorKt confirmed 
```



如果你想自己构建

构建
```
kotlinc Refactor.kt Utils.kt flat/FlatFilesWhichEqual.kt  -include-runtime -d refactor.jar 
```