## RoomDatabase Demo

## Features

1. Implement the Model-View-ViewModel (MVVM) architecture using Kotlin Flow to enhance code separation and maintainability.
2. Integrate Room library to save user data on the local database and read user data to display in a list.

## Feature 1: Room

In this exercise, you will develop a simple Android application that implements the MVVM architecture using Kotlin Flow and Room to persist user data.

**Prerequisites:**

- Ensure you are using Android Studio Koala 2024.
- Your project targets Android 14 (API 34).

### Step 1: Set Up the Project

1.     **Create a New Project**

- Open Android Studio and create a new project.
- Name the project `SimpleMVVMRoomApp`.
- Select `Empty Activity` and set the language to Kotlin.

2.     **Add Required Dependencies**

- Open `build.gradle.kts` (Project level) and copy this over:

3.  plugins **{**
    alias(_libs_._plugins_._android_._application_)  
    alias(_libs_._plugins_._jetbrains_._kotlin_._android_)  
    id("kotlin-kapt")  
  
**}**

_android_ **{**
    namespace = "com.example.simplemvvmroomapp"
    compileSdk = 34

    defaultConfig **{**
        applicationId = "com.example.simplemvvmroomapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables **{**
            useSupportLibrary = true
        **}  
    }**

    buildTypes **{**
        _release_ **{**
            isMinifyEnabled = false
            proguardFiles(  
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )  
        **}  
    }**
    compileOptions **{**
        sourceCompatibility = JavaVersion._VERSION_1_8_
        targetCompatibility = JavaVersion._VERSION_1_8_
    **}**
    _kotlinOptions_ **{**
        jvmTarget = "1.8"
    **}**
    buildFeatures **{**
        compose = true
    **}**
    composeOptions **{**
        kotlinCompilerExtensionVersion = "1.5.1"
    **}**
    packaging **{**
        resources **{**
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        **}  
    }  
}**

_dependencies_ **{**

    _implementation_(_libs_._androidx_._core_._ktx_)
    _implementation_(_libs_._androidx_._lifecycle_._runtime_._ktx_)
    _implementation_(_libs_._androidx_._activity_._compose_)
    _implementation_(platform(_libs_._androidx_._compose_._bom_))
    _implementation_(_libs_._androidx_._ui_)
    _implementation_(_libs_._androidx_._ui_._graphics_)
    _implementation_(_libs_._androidx_._ui_._tooling_._preview_)
    _implementation_(_libs_._androidx_._material3_)

    // Room dependencies
    _implementation_(_libs_._androidx_._room_._runtime_)
    _implementation_(_libs_._androidx_._room_._ktx_)
    _kapt_(_libs_._androidx_._room_._compiler_)

    _testImplementation_(_libs_._junit_)
    _androidTestImplementation_(_libs_._androidx_._junit_)
    _androidTestImplementation_(_libs_._androidx_._espresso_._core_)
    _androidTestImplementation_(platform(_libs_._androidx_._compose_._bom_))
    _androidTestImplementation_(_libs_._androidx_._ui_._test_._junit4_)
    _debugImplementation_(_libs_._androidx_._ui_._tooling_)
    _debugImplementation_(_libs_._androidx_._ui_._test_._manifest_)  
**}**

Open the **libs,versions.toml** file and copy this over:

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

androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomRuntime" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomKtx" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomCompiler" }  
  
  
[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
jetbrains-kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }

#### Step 2: Implementing MVVM Components with Kotlin Flow and Room

1.     **Model Layer**

- Create a data class for the model. In this example, we will create a simple `User` data class.

package com.example.simplemvvmroomapp

import androidx.room.Entity
import androidx.room.PrimaryKey  
  
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int  
)

2.     **DAO Interface**

- Create a DAO interface to manage database operations.

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

3.     **Room Database**

- Create a Room database class to provide a database instance.

package com.example.simplemvvmroomapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//
@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null  
  
        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: _synchronized_(this) **{**
                val instance = Room.databaseBuilder(  
                    context._applicationContext_,  
                    UserDatabase::class._java_,
                    "user_database"
                ).build()
                INSTANCE = instance  
                instance  
            **}**
        }  
    }  
}

4.     **Repository**

- Create a repository class to manage data operations.

package com.example.simplemvvmroomapp

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)  
    }  
}

5.     **ViewModel Layer**

- Create a ViewModel class that uses Kotlin Flow to provide data to the UI.

package com.example.simplemvvmroomapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle._viewModelScope_
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val users: StateFlow<List<User>> = repository.allUsers
        ._stateIn_(
            scope = _viewModelScope_,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _emptyList_()  
        )

    fun insertUser(user: User) = _viewModelScope_._launch_ **{**
        repository.insertUser(user)  
    **}**
}

- Create a `UserViewModelFactory` to instantiate the ViewModel.

package com.example.simplemvvmroomapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//
class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class._java_)) {  
            UserViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")  
        }  
    }  
}

6.     **View Layer**

- Update the `MainActivity` to use the ViewModel and display the data using Jetpack Compose.

package com.example.simplemvvmroomapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplemvvmroomapp.ui.theme.SimpleMVVMRoomAppTheme

import android.content.Intent

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform._LocalContext_
import androidx.compose.ui.unit.dp
//
class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by _viewModels_ **{**
        UserViewModelFactory(UserRepository(UserDatabase.getDatabase(_application_).userDao()))  
    **}**

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _setContent_ **{**
            SimpleMVVMRoomAppTheme **{**
                Surface(
                    modifier = Modifier._fillMaxSize_(),
                    color = MaterialTheme.colorScheme.background
                ) **{**
                    UserListScreen(userViewModel)  
                **}  
            }  
        }**
    }  
}

@Composable
fun UserListScreen(userViewModel: UserViewModel) {
    val users by userViewModel.users.collectAsState()
    val (name, setName) = remember **{** _mutableStateOf_("") **}**
    val (age, setAge) = remember **{** _mutableStateOf_("") **}**

    Column(modifier = Modifier._padding_(16._dp_)) **{**
        Text(text = "User List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier._height_(8._dp_))
        TextField(
            value = name,
            onValueChange = setName,
            label = **{** Text("Name") **}**,
            modifier = Modifier._fillMaxWidth_()  
        )
        Spacer(modifier = Modifier._height_(8._dp_))
        TextField(
            value = age,
            onValueChange = setAge,
            label = **{** Text("Age") **}**,
            modifier = Modifier._fillMaxWidth_()  
        )
        Spacer(modifier = Modifier._height_(8._dp_))
        Button(onClick = **{**
            val user = User(name = name, age = age._toInt_())  
            userViewModel.insertUser(user)  
            setName("")  
            setAge("")  
        **}**) **{**
            Text("Add User")  
        **}**
        Spacer(modifier = Modifier._height_(16._dp_))
        LazyColumn **{**
            _items_(users) **{** user **->**
                UserItem(user)  
            **}  
        }  
    }**
}

7.    
  
@Composable
fun UserItem(user: User) {
    Column(modifier = Modifier._padding_(8._dp_)) **{**
        Text(text = "Name: ${user.name}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Age: ${user.age}", style = MaterialTheme.typography.bodyMedium)  
    **}**
}

#### Step 3: Run and Test the Application

1.     **Build and Run**

- Connect an Android Virtual Device (AVD) or a physical Android device.
- Run the application and verify that it displays a list of users and allows adding new users.

|   |
|---|
||

2.     **Check Data Persistence**

- Ensure that the `UserViewModel` fetches the list of users from the repository and displays it in the UI using Jetpack Compose and Kotlin Flow.
- Verify that new users are persisted in the Room database and displayed in the list.

### Demonstration

Please demonstrate the exercises at the end of the lab session. Ensure that your application runs successfully on an Android device or emulator and that all functionalities are working as expected.

---

**Note:** Use the references provided in the Week 8 lecture slides to help you with MVVM architecture, Jetpack Compose, and Room integration.