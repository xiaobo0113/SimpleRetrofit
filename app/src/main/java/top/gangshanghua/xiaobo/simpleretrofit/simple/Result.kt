package top.gangshanghua.xiaobo.simpleretrofit.simple

data class Result<T>(val message: String?, val code: Int, val data: T?)
