/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package middol.sys

import grails.compiler.GrailsCompileStatic
/**
* @Description:    数据操作日志
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:37
* @Version:        1.0
 *
 * OperationLogs are reported to the AuditLog table.
 * This requires you to set up a table or allow
 * Grails to create a table for you. (e.g. DDL or db-migration plugin)
 */

class OperationLog implements Serializable {
    static final long serialVersionUID = 1L

    String actor
    String uri
    String className
    String persistedObjectId
    Long persistedObjectVersion = 0

    String eventName
    //String propertyName
    String oldJson
    String newJson

    Date dateCreated
    Date lastUpdated

    static constraints = {
        actor(nullable: true)
        uri(nullable: true)
        className(nullable: true)
        persistedObjectId(nullable: true)
        persistedObjectVersion(nullable: true)
        eventName(nullable: true)
        //propertyName(nullable: true)
        oldJson(nullable: true)
        newJson(nullable: true)
    }

    static mapping = {

        // Set similiar when you used "auditLog.tablename" in < 1.1.0 plugin version.
//        table 'audit_log'

        // Remove when you used "auditLog.cacheDisabled = true" in < 1.1.0 plugin version.
        cache usage: 'read-only', include: 'non-lazy'

        // Set similiar when you used "auditLog.useDatasource" in < 1.1.0 plugin version.
        // datasource "yourdatasource"

        // Set similiar when you used "auditLog.idMapping" in < 1.1.0 plugin version. Example:
        // id generator:"uuid2", type:"string", "length:36"

        // no HQL queries package name import (was default in 1.x version)
        // autoImport false

        version false
        autoTimestamp false

        // for large column support (as in < 1.0.6 plugin versions), use this
        comment "数据操作日志"
        actor comment: "操作人"
        uri comment: "请求路径"
        className comment: "域类名"
        persistedObjectId comment: "操作的数据id"
        persistedObjectVersion comment: "操作的数据版本"
        eventName comment: "操作类型"
        oldJson comment: "操作前字段数据", type: 'text'
        newJson comment: "操作后字段数据", type: 'text'
        dateCreated comment: "操作时间"
        lastUpdated comment: "更新时间"
    }

    /**
     * Deserializer that maps a stored map onto the object
     * assuming that the keys match attribute properties.
     */
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        def map = input.readObject()
        map.each { k, v -> this."$k" = v }
    }

    /**
     * Because Closures do not serialize we can't send the constraints closure
     * to the Serialize API so we have to have a custom serializer to allow for
     * this object to show up inside a webFlow context.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        def map = [
                id: id,
                dateCreated: dateCreated,
                lastUpdated: lastUpdated,

                actor: actor,
                uri: uri,
                className: className,
                persistedObjectId: persistedObjectId,
                persistedObjectVersion: persistedObjectVersion,

                eventName: eventName,
                //propertyName: propertyName,
                oldJson: oldJson,
                newJson: newJson,
        ]
        out.writeObject(map)
    }

    String toString() {
        String actorStr = actor ? "user ${actor}" : "user ?"
        "audit log ${dateCreated} ${actorStr} " +
                "${eventName} ${className} " +
                "id:${persistedObjectId} version:${persistedObjectVersion}"
    }
}
