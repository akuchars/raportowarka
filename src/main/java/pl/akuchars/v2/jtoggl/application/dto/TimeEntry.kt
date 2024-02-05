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
package pl.akuchars.v2.jtoggl.application.dto

import pl.akuchars.v2.jtoggl.domain.util.DateUtil
import pl.akuchars.v1.kernel.HoursMinutes
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

/**
 *
 * @author Simon Martinelli
 */
class TimeEntry// Tag names
	(jsonString: String?) {
	var id: Long? = null

	val key: String?
		get() = description?.let { "\\w+-\\d+".toRegex().find(it)?.groupValues?.get(0) }
	var description: String? = null
	val descriptionWithoutKey: String?
		get() = description?.replace(key!!, "")
	var start: Date? = null
	var stop: Date? = null
	var duration: Long? = null
	var isBillable: Boolean? = null
	var workspace: Workspace? = null
		private set
	var tag_names: List<String> = ArrayList()
	var created_with: String? = null
	var duronly: Boolean? = null
	var pid: Long? = null
	var wid: Long? = null
	var tid: Long? = null
	var uid: Long? = null
		private set
	var projectName: String? = null

	init {
		val `object` = JSONValue.parse(jsonString) as JSONObject
		id = `object`["id"] as Long?
		description = `object`["description"] as String?
		start = DateUtil.convertStringToDate(`object`["start"] as String?)
		stop = if (`object`.containsKey("end")) {
			DateUtil.convertStringToDate(`object`["end"] as String?)
		} else {
			DateUtil.convertStringToDate(`object`["stop"] as String?)
		}
		duration = if (`object`.containsKey("dur")) {
			`object`["dur"] as Long?
		} else {
			`object`["duration"] as Long?
		}
		isBillable = if (`object`.containsKey("is_billable")) {
			`object`["is_billable"] as Boolean?
		} else {
			`object`["billable"] as Boolean?
		}
		duronly = `object`["duronly"] as Boolean?
		created_with = `object`["created_with"] as String?
		pid = `object`["pid"] as Long?
		wid = `object`["wid"] as Long?
		tid = `object`["tid"] as Long?
		uid = `object`["uid"] as Long?
		val workspaceObject = `object`["workspace"] as JSONObject?
		if (workspaceObject != null) {
			workspace = Workspace(workspaceObject.toJSONString())
		}
		val tagsArray = `object`["tags"] as JSONArray?
		val tags: MutableList<String> = ArrayList()
		if (tagsArray != null) {
			for (arrayObject in tagsArray) {
				tags.add(arrayObject as String)
			}
		}
		tag_names = tags
	}

	fun day(): LocalDate {
		return start!!.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate()
	}

	fun hoursMinutes(): HoursMinutes {
		val durations = duration!!.toDouble()
		return HoursMinutes(durations.toLong())
	}

	private fun toJSONObject(): JSONObject {
		val `object` = JSONObject()
		if (isBillable != null) {
			`object`["billable"] = isBillable
		}
		if (key != null) {
			`object`["description"] = key
		}
		if (duration != null) {
			`object`["duration"] = duration
		}
		if (id != null) {
			`object`["id"] = id
		}
		if (duronly != null) {
			`object`["duronly"] = duronly
		}
		if (start != null) {
			`object`["start"] = DateUtil.convertDateToString(start!!)
		}
		if (stop != null) {
			`object`["stop"] = DateUtil.convertDateToString(stop!!)
		}
		if (created_with != null) {
			`object`["created_with"] = created_with
		}
		if (!tag_names.isEmpty()) {
			val tag_names_arr = JSONArray()
			tag_names_arr.addAll(tag_names)
			`object`["tags"] = tag_names_arr
		}
		if (pid != null) {
			`object`["pid"] = pid
		}
		if (workspace != null) {
			`object`["workspace"] = workspace!!.toJSONObject()
		}
		if (wid != null) {
			`object`["wid"] = wid
		}
		if (tid != null) {
			`object`["tid"] = tid
		}
		return `object`
	}

	fun toJSONString(): String {
		return toJSONObject().toJSONString()
	}

	override fun toString(): String {
		return "TimeEntry{id=$id, description=$key  start=$start, stop=$stop, duration=$duration, billable=$isBillable, workspace=$workspace, tag_names=$tag_names, duronly=$duronly, tid = $tid}"
	}

	override fun equals(other: Any?): Boolean {
		if (other == null) {
			return false
		}
		if (javaClass != other.javaClass) {
			return false
		}
		other as TimeEntry
		return !(id !== other.id && (id == null || id != other.id))
	}

	override fun hashCode(): Int {
		var hash = 3
		hash = 79 * hash + if (id != null) id.hashCode() else 0
		return hash
	}
}