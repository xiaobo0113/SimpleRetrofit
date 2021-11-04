package top.gangshanghua.xiaobo.lib_annotation

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@kotlin.annotation.Target(AnnotationTarget.VALUE_PARAMETER)
annotation class UUIDHeader(val uuid: String = "")