# BeatRush

## Brief
![beatrush-demo](https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExMWhqczVhMHdwNjVkcGZ0M2hsc2JpN2trNXNwOXRhNjY4MnZmZWZuNiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/RFOOgShL5MClgasjF7/giphy.gif)

## Setup Guide

1. clone this repo (複製倉庫)
   > 若你使用的是 GitHub Desktop，\
   請參考此影片：[Git, GitHub, & GitHub Desktop for beginners](https://youtu.be/8Dd7KRpKeaE)。
    ```
    git clone git@github.com:RogelioKG/Duel-Master.git
    ```
2. assets directory (音檔、譜面檔資源包)
    + 請在目錄頂層補上 [assets] 資源包
2. build (建置專案)
    ```
    ./gradlew clean build
    ```
3. run (執行專案)
    ```
    ./gradlew run
    ```
4. package to portable executable (打包成執行檔)
    ```
    ./gradlew jpackage
    ```
    + 打包成果位於 `/build/jpackage/BeatRush` 目錄
    + 請在 `/build/jpackage/BeatRush` 目錄頂層補上 [assets] 資源包



[assets]: https://drive.google.com/file/d/1qCwnRwjOVgL0bkRSsaycZ7uihIk8aPBE/view?usp=sharing
