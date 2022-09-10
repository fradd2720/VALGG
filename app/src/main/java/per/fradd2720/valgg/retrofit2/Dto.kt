package per.fradd2720.valgg.retrofit2

import com.google.gson.annotations.SerializedName

data class Agent(
    @SerializedName("data")
    val agents: List<Data>,
) {
    inner class Data(
        @SerializedName("displayName")
        val name: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("displayIcon")
        val icon: String,

        @SerializedName("fullPortrait")
        val image: String,

        @SerializedName("backgroundGradientColors")
        val colors: List<String>,

        @SerializedName("isPlayableCharacter")
        val isPlayable: Boolean,

        @SerializedName("role")
        val position: Position,

        @SerializedName("abilities")
        val abilities: List<Ability>
    ) {
        inner class Position(
            @SerializedName("displayName")
            val name: String,

            @SerializedName("description")
            val description: String,

            @SerializedName("displayIcon")
            val icon: String,
        )

        inner class Ability(
            @SerializedName("displayName")
            val name: String,

            @SerializedName("description")
            val description: String,

            @SerializedName("displayIcon")
            val icon: String,
        )
    }
}