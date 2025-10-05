package com.example.trendingwallpapers.data

object WallpaperRepository {
    val trendingWallpapers: List<Wallpaper> = listOf(
        Wallpaper(
            id = 1,
            title = "Aurora Boreal Nórdica",
            category = "Naturaleza",
            trendingRank = 1,
            imageUrl = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80",
            description = "Los tonos verdes y púrpura de esta aurora están dominando las redes sociales.",
            photographer = "Johny Goerend"
        ),
        Wallpaper(
            id = 2,
            title = "Neón Metropolitano",
            category = "Urbano",
            trendingRank = 2,
            imageUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=1200&q=80",
            description = "Luces neón futuristas perfectas para pantallas AMOLED.",
            photographer = "Patrick Tomasso"
        ),
        Wallpaper(
            id = 3,
            title = "Olas Pastel",
            category = "Minimalismo",
            trendingRank = 3,
            imageUrl = "https://images.unsplash.com/photo-1523475472560-d2df97ec485c?auto=format&fit=crop&w=1200&q=80",
            description = "Gradientes fluidos en tendencia dentro del diseño digital.",
            photographer = "Icons8"
        ),
        Wallpaper(
            id = 4,
            title = "Horizonte Ciberpunk",
            category = "Sci-Fi",
            trendingRank = 4,
            imageUrl = "https://images.unsplash.com/photo-1526401485004-46910ecc8e51?auto=format&fit=crop&w=1200&q=80",
            description = "Una ciudad futurista inspirada en las series más populares del momento.",
            photographer = "Hugh Han"
        ),
        Wallpaper(
            id = 5,
            title = "Nubes de Algodón",
            category = "Sueños",
            trendingRank = 5,
            imageUrl = "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1200&q=80",
            description = "Texturas suaves y colores cálidos para fondos relajantes.",
            photographer = "Magda Ehlers"
        )
    )
}
