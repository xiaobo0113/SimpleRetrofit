@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package top.gangshanghua.xiaobo.lib_processor

import com.google.auto.service.AutoService
import com.sun.source.util.Trees
import com.sun.tools.javac.model.JavacElements
import com.sun.tools.javac.processing.JavacProcessingEnvironment
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.tree.TreeTranslator
import com.sun.tools.javac.util.Context
import com.sun.tools.javac.util.List
import com.sun.tools.javac.util.Names
import top.gangshanghua.xiaobo.lib_annotation.UUIDHeader
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

/**
 * must in a single lib module, here is lib-processor
 */
@AutoService(Processor::class)
class UUIDHeaderProcessor : AbstractProcessor() {

    private lateinit var elementUtils: JavacElements

    private lateinit var trees: Trees
    private lateinit var context: Context
    private lateinit var maker: TreeMaker
    private lateinit var names: Names

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        elementUtils = processingEnv.elementUtils as JavacElements

        trees = Trees.instance(processingEnv)
        context = (processingEnv as JavacProcessingEnvironment).context
        maker = TreeMaker.instance(context)
        names = Names.instance(context)
    }

    override fun process(
        set: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        if (roundEnvironment.processingOver()) {
            return false
        }

        println("==========================UUIDHeaderProcessor()==========================>")
        for (element in roundEnvironment.getElementsAnnotatedWith(UUIDHeader::class.java)) {
            if (element.kind != ElementKind.PARAMETER) {
                throw IllegalStateException("@UUIDHeader should only be used for parameters.")
            }

            val myTree = trees.getTree(element) as JCTree
            myTree.accept(object : TreeTranslator() {
                override fun visitVarDef(tree: JCTree.JCVariableDecl) {
                    super.visitVarDef(tree)

                    // maker.pos = tree.pos
                    val annotation = maker.Annotation(
                        memberAccess("retrofit2.http.Header"),
                        List.of(memberAccess("\"HEADER_UUID\""))
                    )

                    tree.mods.annotations = tree.mods.annotations.append(annotation)
                    println("visitVarDef(), mods: ${tree.mods}")

                    this.result = tree
                }
            })
        }
        println("<==========================UUIDHeaderProcessor()==========================")

        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(UUIDHeader::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private fun memberAccess(components: String): JCTree.JCExpression {
        val componentArray = components.split("\\.".toRegex()).toTypedArray()
        var expr: JCTree.JCExpression = maker.Ident(
            names.fromString(
                componentArray[0]
            )
        )
        for (i in 1 until componentArray.size) {
            expr = maker.Select(expr, names.fromString(componentArray[i]))
        }
        return expr
    }
}