package online.flowerinsnow.flowerairadar.kook.util

import org.w3c.dom.Node
import java.util.function.Consumer

object XMLUtils {
    fun childNodesForEach(node : Node, action : Consumer<Node>) {
        val childs = node.childNodes
        for (i in 0 until childs.length) {
            action.accept(childs.item(i))
        }
    }
}