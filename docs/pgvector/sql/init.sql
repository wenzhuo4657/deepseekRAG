CREATE DATABASE "ai-rag-knowledge";
-- 启用 pgvector 扩展（如果尚未启用）
CREATE EXTENSION IF NOT EXISTS vector;

-- 创建矢量表，使用 UUID 作为主键
CREATE TABLE VectorStore (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- 使用 UUID 类型，默认值为生成的随机 UUID
                              content TEXT NOT NULL,                        -- 文本内容
                              embedding VECTOR(1536),                       -- 嵌入向量，维度为 1536
                              metadata JSONB,                               -- 元数据，以 JSON 格式存储
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 插入时间，默认为当前时间
);



