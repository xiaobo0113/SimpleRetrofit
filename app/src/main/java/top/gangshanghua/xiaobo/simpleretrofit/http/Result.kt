package top.gangshanghua.xiaobo.simpleretrofit.http

data class Result<T>(val message: String?, val code: Int, val data: T?)
