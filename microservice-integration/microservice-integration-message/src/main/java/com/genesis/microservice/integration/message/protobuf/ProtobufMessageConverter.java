package com.genesis.microservice.integration.message.protobuf;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.googlecode.protobuf.format.HtmlFormat;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.ProtobufFormatter;
import com.googlecode.protobuf.format.XmlFormat;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.protobuf.ExtensionRegistryInitializer;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.ContentTypeResolver;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeType;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Aizhanglin on 2016/8/17.
 *
 */
public class ProtobufMessageConverter extends AbstractMessageConverter {
    private ContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final MediaType PROTOBUF;
    public static final String X_PROTOBUF_SCHEMA_HEADER = "X-Protobuf-Schema";
    public static final String X_PROTOBUF_MESSAGE_HEADER = "X-Protobuf-Message";
    private static final ProtobufFormatter JSON_FORMAT;
    private static final ProtobufFormatter XML_FORMAT;
    private static final ProtobufFormatter HTML_FORMAT;
    private static final ConcurrentHashMap<Class<?>, Method> methodCache;
    private final ExtensionRegistry extensionRegistry;

    static {
        PROTOBUF = new MediaType("application", "x-protobuf", DEFAULT_CHARSET);
        JSON_FORMAT = new JsonFormat();
        XML_FORMAT = new XmlFormat();
        HTML_FORMAT = new HtmlFormat();
        methodCache = new ConcurrentHashMap();
    }
    public ProtobufMessageConverter() {
        this(null);
    }

    public ProtobufMessageConverter(ExtensionRegistryInitializer registryInitializer) {
        super(Arrays.asList(new MediaType[]{PROTOBUF, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}));
        this.extensionRegistry = ExtensionRegistry.newInstance();
        if(registryInitializer != null) {
            registryInitializer.initializeExtensionRegistry(this.extensionRegistry);
        }

    }
    @Override
    public void setSerializedPayloadClass(Class<?> payloadClass) {
          throw new UnsupportedOperationException("Payload class must be byte[] : "+ payloadClass);
    }
    @Override
    protected boolean supports(Class<?> aClass) {
        return Message.class.isAssignableFrom(aClass);
    }

    //读取byte[] to ProtoBuf对象
    @Override
    protected Object convertFromInternal(org.springframework.messaging.Message<?> message, Class<?> targetClass, Object conversionHint) {
        MessageHeaders headers = message.getHeaders();
        MimeType contentType = this.getMimeType(headers);
        if(contentType == null) {
            contentType = this.getDefaultContentType(message);
        }
        Charset charset = contentType.getCharset();
        if(charset == null) {
            charset = DEFAULT_CHARSET;
        }
        ByteArrayInputStream inputStream=new ByteArrayInputStream((byte[]) message.getPayload());
        try {
            Message.Builder msgBuilder = getMessageBuilder((Class<? extends Message>) targetClass);
            if(MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
                InputStreamReader reader = new InputStreamReader(inputStream, charset);
                TextFormat.merge(reader, this.extensionRegistry, msgBuilder);
            } else if(MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                JSON_FORMAT.merge(inputStream, charset, this.extensionRegistry, msgBuilder);
            } else if(MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
                XML_FORMAT.merge(inputStream, charset, this.extensionRegistry, msgBuilder);
            } else {
                msgBuilder.mergeFrom(inputStream, this.extensionRegistry);
            }

            return msgBuilder.build();
        } catch (Exception var7) {
            throw new HttpMessageNotReadableException("Could not read Protobuf message: " + var7.getMessage(), var7);
        }
    }

    @Override
    protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
        MimeType contentType = this.getMimeType(headers);
        if(contentType == null) {
            contentType = this.getDefaultContentType(payload);
        }

        Charset charset = contentType.getCharset();
        if(charset == null) {
            charset = DEFAULT_CHARSET;
        }
        Message message= (Message) payload;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        if(MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, charset);

            try {
                TextFormat.print(message, outputStreamWriter);
                outputStreamWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
            try {
                JSON_FORMAT.print(message, byteArrayOutputStream, charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
            try {
                XML_FORMAT.print(message, byteArrayOutputStream, charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(MediaType.TEXT_HTML.isCompatibleWith(contentType)) {
            try {
                HTML_FORMAT.print(message, byteArrayOutputStream, charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(PROTOBUF.isCompatibleWith(contentType)) {
            try {
                FileCopyUtils.copy(message.toByteArray(), byteArrayOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static Message.Builder getMessageBuilder(Class<? extends Message> clazz) throws Exception {
        Method method = methodCache.get(clazz);
        if(method == null) {
            method = clazz.getMethod("newBuilder", new Class[0]);
            methodCache.put(clazz, method);
        }

        return (Message.Builder)method.invoke(clazz, new Object[0]);
    }
    protected MimeType getMimeType(MessageHeaders headers) {
        return this.contentTypeResolver != null?this.contentTypeResolver.resolve(headers):null;
    }


}
