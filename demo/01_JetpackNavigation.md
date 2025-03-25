# Mastering Navigation in Android Apps with Jetpack Navigation

## 1. Introduction
- **Navigation is essential** for moving between different screens in Android apps.
- **Jetpack Navigation** simplifies implementing complex navigation patterns.
- **Objectives:**
    - Learn Jetpack Navigation components.
    - Implement navigation using Jetpack Compose.


## 2. Navigation Overview with Jetpack Compose
**Jetpack Compose 导航概述**

- Jetpack Navigation supports both 
	- **XML-based UIs**  🏷️
	- and **Jetpack Compose UIs**.
- **NavController** manages navigation logic.
- **NavHost** acts as the container for destinations.
- Example application: **A simple Pets app** for navigation.



## 3. Setting Up Jetpack Navigation
**设置 Jetpack Navigation**

### Steps:

1. Add Jetpack Navigation Compose dependency:
`libs.versions.toml:`

```toml
compose-navigation = "androidx.navigation:navigation-compose:2.7.2"
```
    
2. Update `build.gradle.kts`:
    
    ```kotlin
    implementation(libs.compose.navigation)
    ```
    
3. Sync the project with Gradle.
4. Create instances of **NavController** and **NavHost**.
5. Define the start destination.

## 4. Defining Navigation Routes Using Sealed Classes
 **使用密封类定义导航路由**
如果页面比较多，可以使用。
否则跳过

- **Sealed classes** ensure type-safe navigation.
- **Example:**
    
    ```kotlin
    sealed class Screens(val route: String) {
        object PetsScreen : Screens("pets")
        object PetDetailsScreen : Screens("petDetails")
    }
    ```
    
- This helps **prevent errors** by restricting routes to predefined options.
这有助于通过将路由限制为预定义选项来 **防止错误**。

## 5. Implementing NavController and NavHost in Compose 
**在 Compose 中实现 NavController 和 NavHost**
即写一个导航配置文件，见demo中的`AppNavigation.kt`
配置两个页面，一个静态，一个根据传入id生成页面

- **`rememberNavController()`** creates a persistent NavController.
- **NavHost** defines the structure of navigation.

## 7. 创建Homepage
在应用中应用 `AppNavigation`

代码详见
- `.\views\HomeScreen.kt`
- `.MainActivity.kt` 在这个位置应用  

## 6. Building the PetsScreen Composable **（构建 PetsScreen 组件）**

- **Scaffold** provides structure for app bars and content.
- **TopAppBar** ensures consistent navigation UI.
- **Example:**
    
    ```kotlin
    @Composable
    fun PetsScreen(navController: NavController) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Pets") }) },
            content = { PetList(navController) }
        )
    }
    ```
    


## 7. Navigating Between Compose Destinations **（在 Compose 组件之间进行导航）**

- **Use `NavController.navigate()`** to move between screens.
- Example: Clicking a pet navigates to `PetDetailsScreen`。

### Example:

```kotlin
@Composable
fun PetList(navController: NavController, pets: List<Cat>) {
    LazyColumn {
        items(pets) { pet ->
            PetListItem(pet, onClick = {
                navController.navigate(Screens.PetDetailsScreen.route)
            })
        }
    }
}
```

- This uses **LazyColumn** to list pets and navigate on click.

## 8. Defining the PetDetailsScreen Composable 

**定义 PetDetailsScreen 组件**

- Displays **detailed pet information**.
- Uses **AsyncImage** for loading images.
- Adds **back navigation** with a back button.
- **Example:**
    
    ```kotlin
    @Composable
    fun PetDetailsScreen(navController: NavController, petId: String) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pet Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            content = { PetDetailsContent(petId) }
        )
    }
    ```
    
- Includes **navigation logic to return to the previous screen**。

## 9. Passing Arguments to Destinations **（向目标传递参数）**

- **Pass data between screens using route arguments**。
- **Example:** Passing pet ID:
    
    ```kotlin
    // Trigger navigation with an argument
    navController.navigate("${Screens.PetDetailsScreen.route}/$petId")
    
    // Define argument in NavHost
    composable(
        "${Screens.PetDetailsScreen.route}/{petId}",
        arguments = listOf(navArgument("petId") { type = NavType.StringType })
    ) { backStackEntry ->
        PetDetailsScreen(navController, backStackEntry.arguments?.getString("petId")!!)
    }
    ```
    
- Ensures **data consistency and type safety**。
