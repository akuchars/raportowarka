package pl.akuchars.v1.jtoggl

import org.json.simple.JSONObject
import org.json.simple.JSONValue

/**
 *
 * @author Simon Martinelli
 */
class Workspace(jsonString: String?) {
	var id: Long? = null
	var name: String? = null
	var premium: Boolean? = null

	init {
		val `object` = JSONValue.parse(jsonString) as JSONObject
		id = `object`["id"] as Long?
		name = `object`["name"] as String?
		premium = `object`["premium"] as Boolean?
	}

	fun toJSONObject(): JSONObject {
		val `object` = JSONObject()
		if (id != null) {
			`object`["id"] = id
		}
		if (name != null) {
			`object`["name"] = name
		}
		if (premium != null) {
			`object`["premium"] = premium
		}
		return `object`
	}

	override fun toString(): String {
		return "Workspace{id=$id, name=$name}"
	}

	override fun equals(other: Any?): Boolean {
		if (other == null) {
			return false
		}
		if (javaClass != other.javaClass) {
			return false
		}
		val other = other as Workspace
		return !(id !== other.id && (id == null || id != other.id))
	}

	override fun hashCode(): Int {
		var hash = 3
		hash = 83 * hash + if (id != null) id.hashCode() else 0
		return hash
	}
}