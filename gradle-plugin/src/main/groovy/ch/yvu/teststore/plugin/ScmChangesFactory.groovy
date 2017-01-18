package ch.yvu.teststore.plugin

class ScmChangesFactory {
    private final TestStorePluginExtension extension

    def ScmChangesFactory(TestStorePluginExtension extension) {
        this.extension = extension
    }

    def createScmChanges() {
        return new ScmChanges(jsonProvider: new FileJsonProvider(filePath: extension.changesFile))
    }
}
