package org.fixme.core.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Json {
	private static Logger 			logger = LoggerFactory.getLogger(Json.class);

	private static final class Null {

		/**
		 * There is only intended to be a single instance of the NULL object,
		 * so the clone method returns itself.
		 *
		 * @return NULL.
		 */

		@Override
		protected final Object clone() {
			return this;
		}

		/**
		 * A Null object is equal to the null value and to itself.
		 *
		 * @param object
		 *            An object to test for nullness.
		 * @return true if the object parameter is the JSONObject.NULL object or
		 *         null.
		 */
		@Override
		public boolean equals(Object object) {
			return object == null || object == this || object.toString().equalsIgnoreCase("null");
		}

		/**
		 * Get the "null" string value.
		 *
		 * @return The string "null".
		 */
		public String toString() {
			return "null";
		}
	}

	/**
	 * It is sometimes more convenient and less ambiguous to have a
	 * <code>NULL</code> object than to use Java's <code>null</code> value.
	 * <code>JSONObject.NULL.equals(null)</code> returns <code>true</code>.
	 * <code>JSONObject.NULL.toString()</code> returns <code>"null"</code>.
	 */
	public static final Object NULL = new Null();

	/**
	 * Current Model json Object
	 */
	private JSONObject obj = null;

	/**
	 * Construct Json by Json
	 * @param obj
	 */
	public Json(Json obj)
	{
		if (obj == null)
			return ;
		this.obj = new JSONObject(obj.toString());
	}

	/**
	 * Construct Json by JsonObject
	 * @param obj
	 */
	public Json(JsonObject obj)
	{
		if (obj == null)
			return ;
		this.obj = new JSONObject(obj.toString());
	}

	/**
	 * Construct Json by String {**JSON CODE***}
	 * @param json
	 */
	public Json(String json)
	{
		if (json == null)
			return ;
		this.obj = new JSONObject(json);
	}

	/**
	 * Construct Json by JSONObject
	 * @param obj
	 */
	public Json(JSONObject obj)
	{
		if (obj == null)
			return ;
		this.obj = new JSONObject(obj.toString());
	}

	/**
	 * Construct new Json {}
	 * @return 
	 */
	public Json()
	{
		this.obj = new JSONObject();
		
	}
	/**
	 * Construct Json by InputStream
	 * @param stream
	 */
	public Json(InputStream stream) {
		if (stream == null)
			return ;
		this.obj =  new JSONObject(stream);
	}

	/**
	 * Construct Json by InputStreamReader
	 * @param stream
	 */
	public Json(InputStreamReader streamReader ) {
		if (streamReader == null)
			return ;
		this.obj =  new JSONObject(streamReader);
	}
	/**
	 * Convert Json to Get JSONObject
	 * @return
	 */
	public JSONObject getJSONObject()
	{
		return (obj);
	}
	
	
	/**
	 * Convert Json to JsonObject
	 * @return
	 */
	public JsonObject getJsonObject()
	{
		return (Json.JSONObjectToJsonObject(obj));
	}

	/**
	 * Put a key/value pair in the JSONObject. If the value is null, then the key 
	 * will be removed from the JSONObject if it is present.
	 * @param key
	 * @param value
	 */
	public Json put(String key, Object value)
	{
		if (value instanceof JsonArray)
		{

			Gson a = new Gson();
			Object b = a.fromJson((JsonArray)value, Object.class);
			JSONArray c = new JSONArray(b.toString());
			int i = 0;
			while (i < c.length()){
				if (c.get(i) == JSONObject.NULL)
					c.put(i, "");
				i++;
			}
			this.obj.put(key, c);
			return this;
		}
		else if (value instanceof JSONArray)
		{
			if(((JSONArray)value).length() == 0)
				this.obj.put(key, new JSONArray());
			((JSONArray)value).forEach((x)->{

				if (x!= null)
					this.append(key, x);
			});
			return this;
		}
		this.obj.put(key, value);
		return this;
	}

	/**
	 * Put on keys the value pair in the JSONObject. If the value is null, then the key 
	 * will be removed from the JSONObject if it is present.
	 * @param keys
	 * @param value
	 * @return 
	 */
	public Json put(String[] keys, Object value)
	{
		for (String key : keys)
			put(key, value);
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject. If the value is null, then the key 
	 * will be removed from the JSONObject if it is present.
	 * @param key
	 * @param value
	 * @return 
	 */
	public Json put(String key, Json value)
	{
		this.obj.put(key, value.getJSONObject());
		return this;
	}

	/**
	 * Put on keys the value pair in the JSONObject. If the value is null, then the key 
	 * will be removed from the JSONObject if it is present.
	 * @param keys
	 * @param value
	 * @return 
	 */
	public Json put(String[] keys, Json value)
	{
		for (String key : keys)
			put(key, value);
		return this;
	}

	/**
	 * Determine if the JSONObject contains a specific key.
	 * @param key
	 * @return
	 */
	public boolean has(String key)
	{
		return this.obj.has(key);
	}

	/**
	 * Determine if the JSONObject contains one specific keys.
	 * @param keys
	 * @return
	 */
	public boolean has(String[] keys)
	{
		try{
			//if (keys == null)
			//return false;
			for (String key : keys)
			{
				if (has(key))
					return true;
			}
		}
		catch(Exception e){
			logger.error("keys = null");
		}
		return false;
	}

	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Object get(String key) throws JSONException
	{
		if (key == null) {
			throw new JSONException("Null key.");
		}
		Object object = this.obj.get(key);
		if (object == null) {
			throw new JSONException("Json[\"" + key + "\"] is null Value");
		}
		return object;
	}

	/**
	 * Get the first occurrence object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Object get(String[] keys) throws JSONException
	{
		for (String key : keys)
		{
			if (key == null) {
				throw new JSONException("Null key.");
			}
			Object object = this.obj.get(key);
			if (object == null)
				continue ;
			return object;
		}
		return null;
	}

	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Boolean getBoolean(String key) throws JSONException
	{
		try {
			return (new Boolean(this.obj.get(key).toString()));
		} catch (Exception e)
		{
			throw new JSONException("Json[\"" + key + "\"] is not a Boolean Value");
		}
	}

	/**
	 * Get the first occurrence object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Boolean getBoolean(String[] keys) throws JSONException
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getBoolean(key));
		}
		throw new JSONException("Json[\"" + keys.toString() + "\"] is not a Boolean Values");
	}

	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public int getInt(String key) throws JSONException
	{
		try {
			return (new Integer(this.obj.getInt(key)));
		} catch (Exception e)
		{
			throw new JSONException("Json[\"" + key + "\"] is not a Int Value");
		}
	}

	/**
	 * Get the first occurrence object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public int getInt(String[] keys) throws JSONException
	{
		for (String key : keys)
		{
			if (this.obj.has(key))return (getInt(key));
		}
		throw new JSONException("Json[\"" + keys.toString() + "\"] is not a Int Values");
	}
	
	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public float getFloat(String key, int ... args) throws JSONException
	{
		try {
			return (new Float(this.obj.getFloat(key)));
		} catch (Exception e)
		{
			throw new JSONException("Json[\"" + key + "\"] is not a Float Value");
		}
	}
	
	/**
	 * Get the first occurrence object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public float getFloat(String[] keys) throws JSONException
	{
		for (String key : keys)
		{
			if (this.obj.has(key))return (getFloat(key));
		}
		throw new JSONException("Json[\"" + keys.toString() + "\"] is not a Float Values");
	}

	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public String getString(String key) throws JSONException
	{
		try {
			return (Json.googleJsontoString(new String(this.obj.get(key).toString())));
		} catch (Exception e)
		{
			throw new JSONException("Json[\"" + key + "\"] is not a String Value");
		}
	}

	/**
	 * Get the first occurrence object associated with a key.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public String getString(String[] keys) throws JSONException
	{
		if (keys == null)
			return (null);
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getString(key));
		}
		return (null);
	}

	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Json getJson(String key) throws JSONException
	{
		try {
			return (new Json(this.obj.get(key).toString()));
		} catch (Exception e)
		{
			throw new JSONException("Json[\"" + key + "\"] is not a Json Value");
		}
	}

	/**
	 * Get the first occurrence object associated with a key.
	 * Parameters:keys A keys string[].Returns:The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public Json getJson(String[] keys) throws JSONException
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getJson(key));
		}
		throw new JSONException("Json[\"" + keys.toString() + "\"] is not a Json Values");
	}

	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getJSONObject(String key) throws JSONException
	{
		try {
			return (this.obj.getJSONObject(key));
		} catch (Exception e)
		{
			this.obj.put(key, new JSONObject());
			return (this.obj.getJSONObject(key));
		}
	}

	/**
	 * Get the first occurrence object associated with a key.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getJSONObject(String[] keys) throws JSONException
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getJSONObject(key));
		}
		throw new JSONException("Json[\"" + keys.toString() + "\"] is not a Json Values");
	}

	/**
	 * Get the value object associated with a key.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public JSONArray getJSONArray(String key) throws JSONException
	{
		try {
			return (new JSONArray(this.obj.get(key).toString()));
		} catch (Exception e)
		{
			throw new JSONException("Json[\"" + key + "\"] is not a JSONArray Value");
		}
	}

	/**
	 * Get the first occurrence object associated with a key.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public JSONArray getJSONArray(String[] keys) throws JSONException
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getJSONArray(key));
		}
		throw new JSONException("Json[\"" + keys.toString() + "\"] is not a JSONArray Values");
	}

	/**
	 * Get an enumeration of the keys of the Json.
	 * @return
	 */
	public Iterator<String> keys()
	{
		return (obj.keys());
	}

	/**
	 * Get a set of keys of the Json.
	 * @return
	 */
	public Set<String> keySet()
	{
		return (obj.keySet());
	}

	/**
	 * Determine if the value associated with the key is null or if there is no value.
	 * @param key
	 * @return
	 */
	public boolean isNull(String key)
	{
		if (obj.isNull(key))
			return true;
		return false;
	}

	/**
	 * Determine if all values associated with the key is null or if there is no value.
	 * @param keys
	 * @return
	 */
	public boolean isNull(String[] keys)
	{
		for (String key : keys)
		{
			if (isNull(key))
				return true;
		}
		return false;
	}

	/**
	 * Remove a name and its value, if present.
	 * @param key
	 * @return
	 */
	public Object remove(String key)
	{
		return (obj.remove(key));
	}

	/**
	 * Remove a name and all values, if present.
	 * @param key
	 * @return
	 */
	public Object remove(String[] keys)
	{
		Object object = null;
		for (String key : keys)
			object = obj.remove(key);
		return (object);
	}

	/**
	 * Determine if two Jsons are similar. They must contain the same set of names which must
	 * be associated with similar values.
	 * @param json
	 * @return
	 */
	public boolean similar(Json json)
	{
		if (obj.similar(json.getJSONObject()))
			return true;
		return false;
	}

	/**
	 * Make a JSON text of this JSONObject. For compactness, no whitespace is added.
	 * If this would not result in a syntactically correct JSON text, then null will be returned instead. 
	 * 
	 * Warning: This method assumes that the data structure is acyclical.
	 * 
	 * Overrides: toString() in Object
	 * 
	 * Returns:a printable, displayable, portable, transmittable representation of the object,
	 * beginning with { (left brace) and ending with } (right brace).
	 */
	public String toString()
	{
		if (obj == null)
			return ("null");
		return (obj.toString());
	}

	/**
	 * Make a JSON text of this JSONObject. For compactness, no whitespace is added.
	 * If this would not result in a syntactically correct JSON text, then null will be returned instead. 
	 * 
	 * Warning: This method assumes that the data structure is acyclical.
	 * 
	 * Overrides: toString() in Object
	 * 
	 * Returns:a printable, displayable, portable, transmittable representation of the object,
	 * beginning with { (left brace) and ending with } (right brace).
	 */
	public String toString(int e)
	{
		if (obj == null)
			return ("null");
		return (obj.toString(e));
	}

	/**
	 * Accumulate values under a key. It is similar to the put method except that
	 * if there is already an object stored under the key then a JSONArray is
	 * stored under the key to hold all of the accumulated values.
	 * If there is already a JSONArray, then the new value is appended to it.
	 * In contrast, the put method replaces the previous value.
	 * If only one value is accumulated that is not a JSONArray,
	 * then the result will be the same as using put.
	 * But if multiple values are accumulated, then the result will be like append.
	 * Parameters:key A key string.value An object to be accumulated under the 
	 * key.Returns:this.Throws:JSONException - If the value is an invalid number or if the key is null.
	 * @param key
	 * @param value
	 */
	public void accumulate(String key, Object value)
	{
		this.obj.accumulate(key, value);
	}

	/**
	 * Append values to the array under a key. If the key does not exist in the JSONObject,
	 * then the key is put in the JSONObject with its value being a JSONArray containing the value parameter.
	 * If the key was already associated with a JSONArray, then the value parameter is appended to it.
	 * Parameters:key A key string.value An object to be accumulated under the
	 * key.Returns:this.Throws:JSONException - If the key is null or if the current
	 * value associated with the key is not a JSONArray.
	 * @param key
	 * @param value
	 */
	public void append(String key, Object value)
	{
		if (value instanceof JsonArray)
		{
			value = new JSONArray(Json.googleJsontoString(value).replace("\\\"", "\""));
		}
		this.obj.append(key, value);
	}

	/**
	 * Get the value object associated with a key.
	 * Parameters:key A key string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Json optJson(String key)
	{
		if (this.obj.has(key))
			return (getJson(key));
		return null;
	}

	/**
	 * Get the value object associated with a keys.
	 * Parameters:keys A keys string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public Json optJson(String[] keys)
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getJson(key));
		}
		return null;
	}

	/**
	 * Get the value object associated with a key.
	 * Parameters:key A key string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public String optString(String key)
	{
		if (this.obj.has(key))
			return (getString(key));
		return null;
	}

	/**
	 * Get the value object associated with a keys.
	 * Parameters:keys A keys string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public String optString(String[] keys)
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getString(key));
		}
		return null;
	}

	/**
	 * Get the value object associated with a key.
	 * Parameters:key A key string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Object opt(String key)
	{
		if (this.obj.has(key))
			return (get(key));
		return null;
	}

	/**
	 * Get the value object associated with a keys.
	 * Parameters:keys A keys string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public Object opt(String[] keys)
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (get(key));
		}
		return null;
	}

	/**
	 * Get the value object associated with a key.
	 * Parameters:key A key string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public JSONArray optJSONArray(String key)
	{
		if (this.obj.has(key))
			return (getJSONArray(key));
		return null;
	}

	/**
	 * Get the value object associated with a keys.
	 * Parameters:keys A keys string.Returns:
	 * The object associated with the key.Throws:JSONException - if the key is not found.
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	public JSONArray optJSONArray(String[] keys)
	{
		for (String key : keys)
		{
			if (this.obj.has(key))
				return (getJSONArray(key));
		}
		return null;
	}

	/**
	 * Get count By keys number of values contained in Json
	 * @param keys
	 * @return
	 */
	public int count(String[] keys)
	{
		int i = 0;
		for (String key : keys)
		{
			if (this.obj.has(key))
				i++;
		}
		return i;
	}

	public int countKeys()
	{
		return (keySet().size());
	}

	/**
	 * Get Element by index
	 * @param i
	 * @return
	 */
	public Object getIndex(int i)
	{
		Object res = null;
		if (this.obj.keySet().isEmpty())
			return null;
		if (this.obj.keySet().size() < i)
			return null;
		Iterator<String> keys = this.obj.keys();
		for (int a = 0; a < (i - 1) && keys.hasNext(); a++)
			keys.next();
		if (keys.hasNext())
			res = keys.next();
		return res;
	}
	
	public static JsonObject JSONObjectToJsonObject(JSONObject JSON)
 	{
 		JsonObject parsed = new JsonObject();
 		JsonParser parser = new JsonParser();
 		parsed = (JsonObject)parser.parse(JSON.toString(4));
 		return parsed;
 	}
	
	public static String googleJsontoString(Object obj)
	{
		if (obj == null)
			return ("");
		String result = obj.toString();
		
		result = result.replace("\"", "");
		if (result == null)
			result = "null";
		return (result);
	}
}
