package akuchars.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class Author (

  @SerializedName("self"        ) var self        : String? = null,
  @SerializedName("name"        ) var name        : String? = null,
  @SerializedName("key"         ) var key         : String? = null,
  @SerializedName("displayName" ) var displayName : String? = null,
  @SerializedName("avatar"      ) var avatar      : String? = null

)