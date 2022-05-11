```
/**
 * @author JoeZhou
 */
public class FilePersistenceConfig implements InitFunc {

    @Override
    public void init() throws Exception {
        String ruleDir = "D:/sentinel/al-user-rules";
        String flowRulePath = ruleDir + "/flow-rule.json";
        String degradeRulePath = ruleDir + "/degrade-rule.json";
        String systemRulePath = ruleDir + "/system-rule.json";
        String authorityRulePath = ruleDir + "/authority-rule.json";
        String paramFlowRulePath = ruleDir + "/param-flow-rule.json";

        this.mkdirIfNotExits(ruleDir);
        this.createFileIfNotExits(flowRulePath);
        this.createFileIfNotExits(degradeRulePath);
        this.createFileIfNotExits(systemRulePath);
        this.createFileIfNotExits(authorityRulePath);
        this.createFileIfNotExits(paramFlowRulePath);

        // 流控规则
        ReadableDataSource<String, List<FlowRule>> flowRuleRds = new FileRefreshableDataSource<>(
                flowRulePath,
                flowRuleListParser
        );
        FlowRuleManager.register2Property(flowRuleRds.getProperty());
        WritableDataSource<List<FlowRule>> flowRuleWds = new FileWritableDataSource<>(
                flowRulePath,
                this::encodeJson
        );
        WritableDataSourceRegistry.registerFlowDataSource(flowRuleWds);

        // 降级规则
        ReadableDataSource<String, List<DegradeRule>> degradeRuleRds = new FileRefreshableDataSource<>(
                degradeRulePath,
                degradeRuleListParser
        );
        DegradeRuleManager.register2Property(degradeRuleRds.getProperty());
        WritableDataSource<List<DegradeRule>> degradeRuleWds = new FileWritableDataSource<>(
                degradeRulePath,
                this::encodeJson
        );
        WritableDataSourceRegistry.registerDegradeDataSource(degradeRuleWds);

        // 系统规则
        ReadableDataSource<String, List<SystemRule>> systemRuleRds = new FileRefreshableDataSource<>(
                systemRulePath,
                systemRuleListParser
        );
        SystemRuleManager.register2Property(systemRuleRds.getProperty());
        WritableDataSource<List<SystemRule>> systemRuleWds = new FileWritableDataSource<>(
                systemRulePath,
                this::encodeJson
        );
        WritableDataSourceRegistry.registerSystemDataSource(systemRuleWds);

        // 授权规则
        ReadableDataSource<String, List<AuthorityRule>> authorityRuleRds = new FileRefreshableDataSource<>(
                authorityRulePath,
                authorityRuleListParser
        );
        AuthorityRuleManager.register2Property(authorityRuleRds.getProperty());
        WritableDataSource<List<AuthorityRule>> authorityRuleWds = new FileWritableDataSource<>(
                authorityRulePath,
                this::encodeJson
        );
        WritableDataSourceRegistry.registerAuthorityDataSource(authorityRuleWds);

        // 热点参数规则
        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleRds = new FileRefreshableDataSource<>(
                paramFlowRulePath,
                paramFlowRuleListParser
        );
        ParamFlowRuleManager.register2Property(paramFlowRuleRds.getProperty());
        WritableDataSource<List<ParamFlowRule>> paramFlowRuleWds = new FileWritableDataSource<>(
                paramFlowRulePath,
                this::encodeJson
        );
        ModifyParamFlowRulesCommandHandler.setWritableDataSource(paramFlowRuleWds);
    }

    private Converter<String, List<FlowRule>> flowRuleListParser = source -> JSON.parseObject(source,
            new TypeReference<List<FlowRule>>() {
            }
    );
    private Converter<String, List<DegradeRule>> degradeRuleListParser = source ->
            JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
            }
    );
    private Converter<String, List<SystemRule>> systemRuleListParser = source ->
            JSON.parseObject(source, new TypeReference<List<SystemRule>>() {
            }
    );

    private Converter<String, List<AuthorityRule>> authorityRuleListParser = source ->
            JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {
            }
    );

    private Converter<String, List<ParamFlowRule>> paramFlowRuleListParser = source ->
            JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
            }
    );

    private void mkdirIfNotExits(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @SneakyThrows
    private void createFileIfNotExits(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }
}
```