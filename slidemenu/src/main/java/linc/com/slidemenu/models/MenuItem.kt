package linc.com.slidemenu.models

// TODO: 10.11.20 check for same keys -> remove duplicates 
data class MenuItem(
    internal val resource: Int,
    internal val horizontalPosition: Int,
    internal val section: Section,
    internal val key: String,
    internal val enable: Boolean = true
) {
    companion object {
        const val START = 0
        const val CENTER = 1
        const val END = 3
    }
}