## 安装库


#### `libs.versions.toml`

```
[versions]
...
work = "2.8.1"

[libraries]
...
work-runtime = { module = "androidx.work:work-runtime-ktx", version.ref = "work" }
workmanager-koin = { module = "io.insert-koin:koin-androidx-workmanager", version.ref = "koin" }
```
#### app `build.gradle.kts`

dependencies {
    //workmanager
    implementation(libs.work.runtime)
    implementation(libs.workmanager.koin)
}
```