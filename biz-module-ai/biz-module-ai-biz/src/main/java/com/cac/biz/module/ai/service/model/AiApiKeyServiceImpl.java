package com.cac.biz.module.ai.service.model;

import com.cac.biz.framework.ai.core.enums.AiPlatformEnum;
import com.cac.biz.framework.ai.core.factory.AiModelFactory;
import com.cac.biz.framework.ai.core.factory.AiVectorStoreFactory;
import com.cac.biz.framework.ai.core.model.midjourney.api.MidjourneyApi;
import com.cac.biz.framework.ai.core.model.suno.api.SunoApi;
import com.cac.biz.framework.common.enums.CommonStatusEnum;
import com.cac.biz.framework.common.pojo.PageResult;
import com.cac.biz.framework.common.util.object.BeanUtils;
import com.cac.biz.module.ai.controller.admin.model.vo.apikey.AiApiKeyPageReqVO;
import com.cac.biz.module.ai.controller.admin.model.vo.apikey.AiApiKeySaveReqVO;
import com.cac.biz.module.ai.dal.dataobject.model.AiApiKeyDO;
import com.cac.biz.module.ai.dal.mysql.model.AiApiKeyMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.cac.biz.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cac.biz.module.ai.enums.ErrorCodeConstants.*;

/**
 * AI API 密钥 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AiApiKeyServiceImpl implements AiApiKeyService {

    @Resource
    private AiApiKeyMapper apiKeyMapper;

    @Resource
    private AiModelFactory modelFactory;
    @Resource
    private AiVectorStoreFactory vectorFactory;

    @Override
    public Long createApiKey(AiApiKeySaveReqVO createReqVO) {
        // 插入
        AiApiKeyDO apiKey = BeanUtils.toBean(createReqVO, AiApiKeyDO.class);
        apiKeyMapper.insert(apiKey);
        // 返回
        return apiKey.getId();
    }

    @Override
    public void updateApiKey(AiApiKeySaveReqVO updateReqVO) {
        // 校验存在
        validateApiKeyExists(updateReqVO.getId());
        // 更新
        AiApiKeyDO updateObj = BeanUtils.toBean(updateReqVO, AiApiKeyDO.class);
        apiKeyMapper.updateById(updateObj);
    }

    @Override
    public void deleteApiKey(Long id) {
        // 校验存在
        validateApiKeyExists(id);
        // 删除
        apiKeyMapper.deleteById(id);
    }

    private AiApiKeyDO validateApiKeyExists(Long id) {
        AiApiKeyDO apiKey = apiKeyMapper.selectById(id);
        if (apiKey == null) {
            throw exception(API_KEY_NOT_EXISTS);
        }
        return apiKey;
    }

    @Override
    public AiApiKeyDO getApiKey(Long id) {
        return apiKeyMapper.selectById(id);
    }

    @Override
    public AiApiKeyDO validateApiKey(Long id) {
        AiApiKeyDO apiKey = validateApiKeyExists(id);
        if (CommonStatusEnum.isDisable(apiKey.getStatus())) {
            throw exception(API_KEY_DISABLE);
        }
        return apiKey;
    }

    @Override
    public PageResult<AiApiKeyDO> getApiKeyPage(AiApiKeyPageReqVO pageReqVO) {
        return apiKeyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AiApiKeyDO> getApiKeyList() {
        return apiKeyMapper.selectList();
    }

    // ========== 与 spring-ai 集成 ==========

    @Override
    public ChatModel getChatModel(Long id) {
        AiApiKeyDO apiKey = validateApiKey(id);
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(apiKey.getPlatform());
        return modelFactory.getOrCreateChatModel(platform, apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public ImageModel getImageModel(AiPlatformEnum platform) {
        AiApiKeyDO apiKey = apiKeyMapper.selectFirstByPlatformAndStatus(platform.getPlatform(), CommonStatusEnum.ENABLE.getStatus());
        if (apiKey == null) {
            throw exception(API_KEY_IMAGE_NODE_FOUND, platform.getName());
        }
        return modelFactory.getOrCreateImageModel(platform, apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public MidjourneyApi getMidjourneyApi() {
        AiApiKeyDO apiKey = apiKeyMapper.selectFirstByPlatformAndStatus(
                AiPlatformEnum.MIDJOURNEY.getPlatform(), CommonStatusEnum.ENABLE.getStatus());
        if (apiKey == null) {
            throw exception(API_KEY_MIDJOURNEY_NOT_FOUND);
        }
        return modelFactory.getOrCreateMidjourneyApi(apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public SunoApi getSunoApi() {
        AiApiKeyDO apiKey = apiKeyMapper.selectFirstByPlatformAndStatus(
                AiPlatformEnum.SUNO.getPlatform(), CommonStatusEnum.ENABLE.getStatus());
        if (apiKey == null) {
            throw exception(API_KEY_SUNO_NOT_FOUND);
        }
        return modelFactory.getOrCreateSunoApi(apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public EmbeddingModel getEmbeddingModel(Long id) {
        AiApiKeyDO apiKey = validateApiKey(id);
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(apiKey.getPlatform());
        return modelFactory.getOrCreateEmbeddingModel(platform, apiKey.getApiKey(), apiKey.getUrl());
    }

    @Override
    public VectorStore getOrCreateVectorStore(Long id) {
        AiApiKeyDO apiKey = validateApiKey(id);
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(apiKey.getPlatform());
        return vectorFactory.getOrCreateVectorStore(getEmbeddingModel(id), platform, apiKey.getApiKey(), apiKey.getUrl());
    }

}