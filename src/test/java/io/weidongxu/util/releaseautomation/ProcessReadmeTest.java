package io.weidongxu.util.releaseautomation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProcessReadmeTest {

    @Test
    public void testDotNetReadme() {
        // DotNet, major to minor
        {
            Context context = new Context("1.27.0", "1.27.2");
            List<String> lines = new ArrayList<>();

            String line = "| 1.27              | [1.27](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.27.0)      | Tagged release for 1.27 version of Azure management libraries |";
            lines = DotNetRelease.processReadmeLine(context, lines, line);

            Assertions.assertEquals(2, lines.size());
            Assertions.assertEquals("| 1.27.2              | [1.27.2](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.27.2)      | Tagged release for 1.27.2 version of Azure management libraries |", lines.get(0));
        }

        // DotNet, minor to major
        {
            Context context = new Context("1.26.1", "1.27.0");
            List<String> lines = new ArrayList<>();

            String line = "| 1.26.1              | [1.26.1](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.26.1)      | Tagged release for 1.26.1 version of Azure management libraries |";
            lines = DotNetRelease.processReadmeLine(context, lines, line);

            Assertions.assertEquals(2, lines.size());
            Assertions.assertEquals("| 1.27              | [1.27](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.27.0)      | Tagged release for 1.27 version of Azure management libraries |", lines.get(0));
        }

        // DotNet, major to major
        {
            Context context = new Context("1.26.0", "1.27.0");
            List<String> lines = new ArrayList<>();

            String line = "| 1.26              | [1.26](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.26.0)      | Tagged release for 1.26 version of Azure management libraries |";
            lines = DotNetRelease.processReadmeLine(context, lines, line);

            Assertions.assertEquals(2, lines.size());
            Assertions.assertEquals("| 1.27              | [1.27](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.27.0)      | Tagged release for 1.27 version of Azure management libraries |", lines.get(0));
        }

        // DotNet, minor to minor
        {
            Context context = new Context("1.27.1", "1.27.2");
            List<String> lines = new ArrayList<>();

            String line = "| 1.27.1              | [1.27.1](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.27.1)      | Tagged release for 1.27.1 version of Azure management libraries |";
            lines = DotNetRelease.processReadmeLine(context, lines, line);

            Assertions.assertEquals(2, lines.size());
            Assertions.assertEquals("| 1.27.2              | [1.27.2](https://github.com/Azure/azure-libraries-for-net/releases/tag/Fluent-v1.27.2)      | Tagged release for 1.27.2 version of Azure management libraries |", lines.get(0));
        }

        // Java, previous version
        {
            Context context = new Context("1.27.0", "1.27.2");
            List<String> lines = new ArrayList<>();

            String line = "If you are migrating your code from 1.26.0 to 1.27.0, you can use these release notes for [preparing your code for 1.27.0 from 1.26.0](./notes/prepare-for-1.27.0.md).";
            lines = JavaRelease.processReadmeLine(context, lines, line);

            Assertions.assertEquals(1, lines.size());
            Assertions.assertEquals("If you are migrating your code from 1.27.0 to 1.27.2, you can use these release notes for [preparing your code for 1.27.2 from 1.27.0](./notes/prepare-for-1.27.2.md).", lines.get(0));
        }

        // Java
        {
            Context context = new Context("1.27.0", "1.27.2");
            List<String> lines = new ArrayList<>();

            String line = "| 1.27.0       | [1.27.0](https://github.com/Azure/azure-libraries-for-java/tree/v1.27.0)               | Tagged release for 1.27.0 version of Azure management libraries |";
            lines = JavaRelease.processReadmeLine(context, lines, line);

            Assertions.assertEquals(2, lines.size());
            Assertions.assertEquals("| 1.27.2       | [1.27.2](https://github.com/Azure/azure-libraries-for-java/tree/v1.27.2)               | Tagged release for 1.27.2 version of Azure management libraries |", lines.get(0));
        }
    }
}
