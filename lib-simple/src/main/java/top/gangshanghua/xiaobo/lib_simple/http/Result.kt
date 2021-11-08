package top.gangshanghua.xiaobo.lib_simple.http

data class Result<T>(val message: String?, val code: Int, val data: T?)
