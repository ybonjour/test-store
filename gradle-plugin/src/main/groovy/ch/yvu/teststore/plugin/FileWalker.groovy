package ch.yvu.teststore.plugin

import java.util.regex.Pattern

import static com.google.common.base.Charsets.UTF_8

class FileWalker {
    String baseDir
    Pattern pattern

    def walkFileContents(visitor) {
        new File(baseDir).eachFileRecurse {
            if (it.isFile() && it.path ==~ pattern) {
                visitor(it.absolutePath, it.getText(UTF_8.name()))
            }
        }
    }
}
