package pl.akuchars.jtoggl

import org.json.simple.JSONObject
import org.json.simple.JSONValue

/**
 *
 * @author Simon Martinelli
 */
class Project {
	var id: Long? = null
	var name: String? = null
	var isBillable: Boolean? = null
	var workspace: Workspace? = null
	private var active: Boolean? = null
	private var is_private: Boolean? = null
	private var template: Boolean? = null
	var cid: Long? = null

	constructor(jsonString: String?) {
		val `object` = JSONValue.parse(jsonString) as JSONObject
		id = `object`["id"] as Long?
		name = `object`["name"] as String?
		isBillable = `object`["billable"] as Boolean?
		active = `object`["active"] as Boolean?
		is_private = `object`["is_private"] as Boolean?
		template = `object`["template"] as Boolean?
		cid = `object`["cid"] as Long?
		val workspaceObject = `object`["workspace"] as JSONObject?
		if (workspaceObject != null) {
			workspace = Workspace(workspaceObject.toJSONString())
		}
	}

	fun toJSONObject(): JSONObject {
		val `object` = JSONObject()
		if (id != null) {
			`object`["id"] = id
		}
		if (name != null) {
			`object`["name"] = name
		}
		if (isBillable != null) {
			`object`["billable"] = isBillable
		}
		if (active != null) {
			`object`["active"] = active
		}
		if (is_private != null) {
			`object`["is_private"] = is_private
		}
		if (template != null) {
			`object`["template"] = template
		}
		if (cid != null) {
			`object`["client"] = cid
		}
		if (workspace != null) {
			`object`["workspace"] = workspace!!.toJSONObject()
		}
		return `object`
	}

	override fun toString(): String {
		return "Project{id=$id, name=$name, active=$active, is_private=$is_private, template=$template, billable=$isBillable, cid=$cid, workspace=$workspace}"
	}

	override fun equals(other: Any?): Boolean {
		if (other == null) {
			return false
		}
		if (javaClass != other.javaClass) {
			return false
		}
		other as Project
		return !(id !== other.id && (id == null || id != other.id))
	}

	override fun hashCode(): Int {
		var hash = 3
		hash = 71 * hash + if (id != null) id.hashCode() else 0
		return hash
	}
}