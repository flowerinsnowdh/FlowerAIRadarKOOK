// TODO 刚迁过来 未实现内容

package online.flowerinsnow.flowerairadar.kook.manager

object PermissionManager
//    fun isUserPermissionSet(qq: String, permission: String, scope: String): Boolean {
//        checkBasePermission(qq)
//        val permissions = json.getJSONArray(qq)
//        for (i in 0 until permissions.size) {
//            val perm = permissions.getJSONObject(i)
//            if ((permission.equals(perm.getString("permission"), true) || perm.getString("permission")
//                    .equals("root", true)) && scopeMatch(scope, perm.getString("scope"))
//            ) {
//                return true
//            }
//        }
//        return false
//    }
//
//    fun checkBasePermission(qq: String) {
////        when {
////            json.getJSONArray(qq) == null -> {
////                val val1 = JSONArray()
////                json[qq] = val1
////                val1.add(basePermission())
////                writeToFile()
////            }
////            json.getJSONArray(qq).size == 0 -> {
////                json.getJSONArray(qq).add(basePermission())
////                json.getJSONArray(qq)
////                writeToFile()
////            }
////        }
//    }
//
//    fun setUserPermission(qq: String, permission: String, scope: String): Boolean {
//        if (isUserPermissionSet(qq, permission, scope)) return false
//        val permissions = json.getJSONArray(qq)
//        val val1 = JSONObject()
//        val1["permission"] = permission
//        val1["scope"] = scope
//        permissions.add(val1)
//        writeToFile()
//        return true
//    }
//
//    fun unsetUserPermission(qq: String, permission: String, scope: String): Boolean {
//        checkBasePermission(qq)
//        val permissions = json.getJSONArray(qq)
//        for (i in 0 until permissions.size) {
//            val perm = permissions.getJSONObject(i)
//            if (permission.equals(perm.getString("permission"), true) && scope.equals(perm.getString("scope"), true)) {
//                permissions.remove(perm)
//                return true
//            }
//        }
//        return false
//    }
//
//    fun getUserPermissions(qq: String): Set<IUserPermission> {
//        checkBasePermission(qq)
//        val val1 = HashSet<IUserPermission>()
//        val permissions = json.getJSONArray(qq)
//        for (i in 0 until permissions.size) {
//            val perm = permissions.getJSONObject(i)
//            val1.add(UserPermissionImpl(perm.getString("permission"), perm.getString("scope")))
//        }
//        return val1
//    }
//
//    private fun basePermission(): JSONObject {
//        val base = JSONObject()
//        base["permission"] = "base"
//        base["scope"] = "#"
//        return base
//    }
//
//    private fun scopeMatch(group: String, scope: String): Boolean {
//        return when (scope) {
//            "#" -> true
//            "@" -> group != "$"
//            "$" -> group == "$"
//            else -> scope == group
//        }
//    }
//}