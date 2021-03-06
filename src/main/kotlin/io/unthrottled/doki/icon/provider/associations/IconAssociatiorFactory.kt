package io.unthrottled.doki.icon.provider.associations

import com.google.gson.Gson
import com.intellij.util.ResourceUtil
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object IconAssociatiorFactory {
  private val gson = Gson()
  fun create(association: Associations): IconAssociatior {
    return try {
      ResourceUtil.getResource(
        IconAssociation::class.java,
        "/associations/", association.fileName
      ).openStream().use {
        val def = gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          AssociationModels::class.java
        )
        IconAssociatior(def.associations.map { associationDefinition ->
          IconAssociation(
            Regex(associationDefinition.associationPattern),
            associationDefinition.iconPath
          )
        })
      }
    } catch (t: Throwable) {
      t.printStackTrace()
      IconAssociatior(listOf())
    }
  }
}