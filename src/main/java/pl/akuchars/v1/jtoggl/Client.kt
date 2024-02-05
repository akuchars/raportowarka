/*
 * jtoggl - Java Wrapper for Toggl REST API https://www.toggl.com/public/api
 *
 * Copyright (C) 2011 by simas GmbH, Moosentli 7, 3235 Erlach, Switzerland
 * http://www.simas.ch
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.akuchars.v1.jtoggl

import org.json.simple.JSONObject
import org.json.simple.JSONValue

/**
 *
 * @author Simon Martinelli
 */
class Client {
	var id: Long? = null
	var name: String? = null
	var hourly_rate: String? = null
	var currency: String? = null
	var workspace: Workspace? = null
	var notes: String? = null

	constructor(jsonString: String?) {
		val `object` = JSONValue.parse(jsonString) as JSONObject
		id = `object`["id"] as Long?
		name = `object`["name"] as String?
		val hrate = `object`["hrate"]
		if (hrate != null) {
			hourly_rate = hrate.toString()
		}
		currency = `object`["cur"] as String?
		notes = `object`["notes"] as String?
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
		if (hourly_rate != null) {
			`object`["hrate"] = hourly_rate
		}
		if (currency != null) {
			`object`["cur"] = currency
		}
		if (notes != null) {
			`object`["notes"] = notes
		}
		if (workspace != null) {
			`object`["workspace"] = workspace!!.toJSONObject()
			`object`["wid"] = workspace!!.id
		}
		return `object`
	}

	fun toJSONString(): String = toJSONObject().toJSONString()


	override fun toString(): String {
		return "Client{id=$id, name=$name, hourly_rate=$hourly_rate, currency=$currency, notes=$notes, workspace=$workspace}"
	}

	override fun equals(other: Any?): Boolean {
		if (other == null) {
			return false
		}
		if (javaClass != other.javaClass) {
			return false
		}
		other as Client
		return id === other.id || !(id == null || id != other.id)
	}

	override fun hashCode(): Int {
		var hash = 7
		hash = 71 * hash + if (id != null) id.hashCode() else 0
		return hash
	}
}