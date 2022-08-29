package online.flowerinsnow.flowerairadar.kook.database

import cc.carm.lib.easysql.api.SQLManager
import cc.carm.lib.easysql.api.SQLTable

enum class DatabaseTables(val table : SQLTable) {
    PERMISSION(
        SQLTable.of("permission",
        arrayOf("`id` INT PRIMARY KEY AUTO_INCREMENT",
        "`user` VARCHAR(16) NOT NULL",
        "`permission` VARCHAR(255) NOT NULL",
        "`scope` VARCHAR(16) NOT NULL",
        "INDEX(`user`, `permission`, `scope`)"))
    )
    ;

    companion object {
        fun createAll(sqlManager : SQLManager) {
            values().forEach {
                it.table.create(sqlManager)
            }
        }
    }
}