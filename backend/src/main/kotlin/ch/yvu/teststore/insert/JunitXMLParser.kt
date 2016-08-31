package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.text.SimpleDateFormat
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.text.Charsets.UTF_8

object JunitXMLParser {
    fun parse(xml: String): List<ResultDto> {
        val testResultNodes = testResultNodes(xml)

        var resultDtos = emptyList<ResultDto>()
        for (i in 0..testResultNodes.length - 1) {
            val testResult = TestResultNode(testResultNodes.item(i))
            if (testResult.skipped) continue


            resultDtos += ResultDto(
                    testName = "${testResult.classname}#${testResult.name}",
                    retryNum = 0,
                    passed = testResult.passed,
                    durationMillis = testResult.durationMs,
                    time = testResult.time,
                    stackTrace = testResult.stackTrace
            )
        }

        return resultDtos
    }

    private fun testResultNodes(xml: String): NodeList {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()

        val document = builder.parse(xml.byteInputStream(UTF_8))

        return document.getElementsByTagName("testcase")
    }

    private class TestResultNode(val node: Node) {
        companion object {
            val timeDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        }

        val name = node.attributes.getNamedItem("name").nodeValue

        val classname = node.attributes.getNamedItem("classname").nodeValue

        val durationMs = Math.round(node.attributes.getNamedItem("time").nodeValue.toDouble() * 1000)

        val passed = !hasChildWithName("failure", "error")

        val skipped = hasChildWithName("skipped")

        val time = timeDateFormat.parse(node.parentNode.attributes.getNamedItem("timestamp").nodeValue)

        val stackTrace = getContentFromChildWithName("failure")

        private fun getContentFromChildWithName(name: String): String? {
            val children = node.childNodes
            for(i in 0..children.length -1) {
                val child = children.item(i)

                if(child.nodeName == name)  return child.textContent
            }

            return null
        }

        private fun hasChildWithName(vararg names: String): Boolean {
            val children = node.childNodes
            for (i in 0..children.length - 1) {
                val child = children.item(i)
                if (names.contains(child.nodeName)) return true
            }

            return false
        }
    }
}