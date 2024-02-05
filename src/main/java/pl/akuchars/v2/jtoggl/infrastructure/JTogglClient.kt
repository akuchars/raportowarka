package pl.akuchars.v2.jtoggl.infrastructure

import io.restassured.RestAssured
import io.restassured.config.HttpClientConfig
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.apache.http.params.CoreConnectionPNames
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import pl.akuchars.v2.jtoggl.application.TogglQueryService
import pl.akuchars.v2.jtoggl.application.dto.Project
import pl.akuchars.v2.jtoggl.application.dto.TimeEntry
import pl.akuchars.v2.jtoggl.application.dto.Workspace
import pl.akuchars.v2.jtoggl.domain.*
import pl.akuchars.v2.jtoggl.domain.util.DateUtil
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class JTogglClient(private val credential: TooglCredential) : TogglQueryService {

    override fun getTimeEntries(startDate: Date?, endDate: Date?): List<TimeEntry> {
        val queryParams = HashMap<String, String?>()
        if (startDate != null && endDate != null) {
            queryParams["start_date"] = DateUtil.convertDateToString(startDate)
            queryParams["end_date"] = DateUtil.convertDateToString(endDate)
        }
        val response = fetch(TIME_ENTRIES, queryParams)
        val data = JSONValue.parse(response) as JSONArray
        val entries: MutableList<TimeEntry> = ArrayList()
        val projects = projects()
        data.forEach { obj ->
            val entryObject = obj as JSONObject
            val timeEntry = TimeEntry(entryObject.toJSONString())
            findProject(projects, timeEntry.pid)?.also { timeEntry.projectName = it }
            entries.add(timeEntry)
        }
        return entries
    }

    override fun workspaces(): List<Workspace> {
        val response = fetch(WORKSPACES)
        val data = JSONValue.parse(response) as JSONArray
        val workspaces: MutableList<Workspace> = ArrayList()
        data.forEach { obj ->
            val entryObject = obj as JSONObject
            workspaces.add(Workspace(entryObject.toJSONString()))
        }
        return workspaces
    }

    override fun projects(): List<Project> {
        val projects: MutableList<Project> = ArrayList()
        val workspaces = workspaces()
        for (workspace in workspaces) {
            val workspaceProjects = getWorkspaceProjects(workspace.id!!)
            for (project in workspaceProjects) {
                project.workspace = workspace
            }
            projects.addAll(workspaceProjects)
        }
        return projects
    }


    override fun getWorkspaceProjects(workspaceId: Long): List<Project> {
        val url = WORKSPACE_PROJECTS.replace(PLACEHOLDER, workspaceId.toString())
        val response = fetch(url)
        val data = JSONValue.parse(response) as JSONArray?
        val projects: MutableList<Project> = ArrayList()
        if (data != null) {
            for (obj in data) {
                val entryObject = obj as JSONObject
                projects.add(Project(entryObject.toJSONString()))
            }
        }
        return projects
    }

    private fun findProject(projects: List<Project>, pid: Long?): String? =
        projects.find { it.id == pid }?.name

    private fun fetch(url: String, params: Map<String, String?> = HashMap()): String {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
        }
        val client = client
        val response = client
            .params(params)[url]
        if (response.statusCode == 403) {
            throw RuntimeException("forbidden")
        } else if (response.statusCode == 404) {
            throw RuntimeException("not found: $url")
        } else if (response.statusCode == 400) {
            throw RuntimeException("bad request: " + response.body.asString())
        }
        return response.body().asString()
    }

    private val client: RequestSpecification
        get() {
            val config = RestAssured.config()
                .httpClient(
                    HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 15 * 1000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 15 * 1000)
                )
            return RestAssured.given()
                .config(config)
                .auth()
                .preemptive()
                .basic(credential.apiToken, "api_token")
                .contentType(ContentType.JSON)
        }

    companion object {
        private const val API_ROOT = "https://api.track.toggl.com/api"
        private const val API_VERSION = 8
        private val API_BASE = String.format("%s/v%d/", API_ROOT, API_VERSION)
        private const val PLACEHOLDER = "{0}"
        private const val SIMPLE_ID_PATH = "/$PLACEHOLDER"
        private val TIME_ENTRIES = API_BASE + "time_entries"
        private val WORKSPACES = API_BASE + "workspaces"
        private val WORKSPACE_BY_ID = WORKSPACES + SIMPLE_ID_PATH
        private val WORKSPACE_USERS = "$WORKSPACE_BY_ID/users"
        private val WORKSPACE_PROJECTS = "$WORKSPACE_BY_ID/projects"
    }
}