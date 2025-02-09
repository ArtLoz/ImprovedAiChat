package com.shadow.deepseekimp.data.repository

import com.shadow.deepseekimp.data.datastore.DataStoreHelper
import com.shadow.deepseekimp.domain.model.chat.AiModel
import javax.inject.Inject

class AiModelRepository @Inject constructor(
    private val dataStoreHelper: DataStoreHelper
) {
    suspend fun getCurrentAiModel():AiModel {
        val aiModelStringValue = dataStoreHelper.getAiModel() ?: return AiModel.DEEEP_SEEK
        return AiModel.valueOf(aiModelStringValue)
    }

    suspend fun saveCurrentAiModel(aiModel: AiModel) {
        dataStoreHelper.setAiModel(aiModel.name)
    }
}