package com.testable.checkstyle.platform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetricsGateSchemaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void goldenFixturePassesRequire100() throws Exception {
        Path fixture = Path.of("src/test/resources/fixtures/platform_checkstyle_golden.json");
        List<String> errors = GateValidator.validate(fixture, true);
        assertTrue(errors.isEmpty(), String.join("\n", errors));
    }

    @Test
    void goldenFixtureHasTwelveTechniques() throws Exception {
        Path fixture = Path.of("src/test/resources/fixtures/platform_checkstyle_golden.json");
        Path techniques = Path.of("src/test/resources/fixtures/classifications.json");
        JsonNode data = MAPPER.readTree(fixture.toFile());
        JsonNode expected = MAPPER.readTree(techniques.toFile());

        assertEquals(12, data.path("metrics").size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(
                    expected.get(i).asText(),
                    data.path("metrics").get(i).path("classification").asText());
        }
    }

    @Test
    void platformFileExistsAfterExport() throws Exception {
        Path platformFile = Path.of(MetricsConstants.PLATFORM_RELATIVE_PATH);
        assertTrue(platformFile.toFile().exists(), "Run mvnw exec:java@export-platform first");
        assertEquals("checkstyle", MAPPER.readTree(platformFile.toFile()).path("tool").asText());
    }

    @Test
    void fractionScoresFailValidation(@TempDir Path tempDir) throws Exception {
        Path fixture = Path.of("src/test/resources/fixtures/platform_checkstyle_golden.json");
        JsonNode bad = MAPPER.readTree(fixture.toFile());
        ((com.fasterxml.jackson.databind.node.ObjectNode) bad).put("ViolationDensityPerKloc", 0.88);

        Path badFile = tempDir.resolve("bad.json");
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(badFile.toFile(), bad);

        List<String> errors = GateValidator.validate(badFile, true);
        assertFalse(errors.isEmpty());
    }
}
