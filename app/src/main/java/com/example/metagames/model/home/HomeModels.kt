package example.metagames.ui.screens.home

data class GameOption(
    val title: String,
    val description: String,
    val buttonText: String,
    val buttonLink: String,
    val isExternal: Boolean
)

data class HowToStep(
    val title: String,
    val description: String,
    val icon: String // usamos emoji simple para no complicar
)
