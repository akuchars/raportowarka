package pl.akuchars.v1.jira.timesheets.dto

import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted? = null, free of charge? = null, to any person obtaining a copy of this software and associated documentation files (the "Software")? = null, to deal in the Software without restriction? = null, including without limitation the rights to use? = null, copy? = null, modify? = null, merge? = null, publish? = null, distribute? = null, sublicense? = null, and/or sell copies of the Software? = null, and to permit persons to whom the Software is furnished to do so? = null, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS"? = null, WITHOUT WARRANTY OF ANY KIND? = null, EXPRESS OR IMPLIED? = null, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY? = null, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM? = null, DAMAGES OR OTHER LIABILITY? = null, WHETHER IN AN ACTION OF CONTRACT? = null, TORT OR OTHERWISE? = null, ARISING FROM? = null, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support? = null, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Issue (

	@SerializedName("reporterKey") val reporterKey : String? = null,
	@SerializedName("issueType") val issueType : String? = null,
	@SerializedName("projectId") val projectId : Int? = null,
	@SerializedName("projectKey") val projectKey : String? = null,
	@SerializedName("iconUrl") val iconUrl : String? = null,
	@SerializedName("versions") val versions : List<String>? = null,
	@SerializedName("summary") val summary : String? = null,
	@SerializedName("components") val components : List<String>? = null,
	@SerializedName("accountKey") val accountKey : String? = null,
	@SerializedName("internalIssue") val internalIssue : Boolean? = null,
	@SerializedName("issueStatus") val issueStatus : String? = null,
	@SerializedName("estimatedRemainingSeconds") val estimatedRemainingSeconds : Int? = null,
	@SerializedName("key") val key : String? = null,
	@SerializedName("id") val id : Int? = null
)