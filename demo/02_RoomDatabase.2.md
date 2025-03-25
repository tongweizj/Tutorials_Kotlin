# Kotlin 项目配置 Room 操作手册

## 1. 项目设置（Project Setup）
### 1.1 创建 Android Studio 项目
1. **打开 Android Studio**，选择 **New Project**。
2. **选择 `Empty Activity` 作为模板**，并设置语言为 **Kotlin**。
3. **命名项目**（例如：`SimpleMVVMRoomApp`）。
4. **选择 Minimum SDK 为 API 26（Android 8.0）或更高**。
5. **点击 Finish 创建项目**。

### 1.2 配置 `build.gradle.kts`
**打开 `build.gradle.kts`（Module: app）并添加以下内容：**

```kotlin
plugins {
    id("com.android.application")
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.simplemvvmroomapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material3:material3:1.1.2")

    // Room 依赖
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
}
```


**打开` libs.versions.toml`**

```kotlin
[versions]
agp = "8.5.1"
kotlin = "1.9.0"
coreKtx = "1.13.1"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
lifecycleRuntimeKtx = "2.8.3"
activityCompose = "1.9.0"
composeBom = "2024.06.00"
// Room
roomRuntime = "2.6.1"
roomKtx = "2.6.1"
roomCompiler = "2.6.1"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
// Room
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomRuntime" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomKtx" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomCompiler" }


[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
jetbrains-kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }

```

**同步项目**（Sync Project with Gradle Files）。

## 2. Room 数据库集成

### 2.1 创建 Model 类
```kotlin
package com.example.simplemvvmroomapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int
)
```

### 2.2 创建 DAO（数据访问对象）
```kotlin
package com.example.simplemvvmroomapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
}
```

### 2.3 创建 Room 数据库类
```kotlin
package com.example.simplemvvmroomapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

## 3. Repository 与 ViewModel 配置
### 3.1 创建 Repository
```kotlin
package com.example.simplemvvmroomapp

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}
```

### 3.2 创建 ViewModel
```kotlin
package com.example.simplemvvmroomapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val users: StateFlow<List<User>> = repository.allUsers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }
}
```

## 4. Jetpack Compose UI 实现
### 4.1 创建 MainActivity
```kotlin
package com.example.simplemvvmroomapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(UserDatabase.getDatabase(application).userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserListScreen(userViewModel)
        }
    }
}
```

### 4.2 创建 UserListScreen 组件
```kotlin
@Composable
fun UserListScreen(userViewModel: UserViewModel) {
    val users by userViewModel.users.collectAsState()
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        TextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
        Button(onClick = {
            userViewModel.insertUser(User(name = name, age = age.toInt()))
            name = ""
            age = ""
        }) { Text("Add User") }
        LazyColumn {
            items(users) { user ->
                Text("Name: ${user.name}, Age: ${user.age}")
            }
        }
    }
}
```

## 5. 运行与测试步骤
1. **连接 Android 设备或模拟器**。
2. **运行应用并测试 UI 交互**。
3. **检查 Room 数据库是否存储用户数据**。
4. **确保应用在重启后仍能读取数据**。

📌 *成功完成后，展示你的应用运行效果！*

