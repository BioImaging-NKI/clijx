package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.MeanZProjection;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * July 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_meanZProjectionAboveThreshold")
public class MeanZProjectionAboveThreshold extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return meanZProjectionAboveThreshold(getCLIJ2() ,(ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
    }

    public static boolean meanZProjectionAboveThreshold(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float threshold) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("threshold", threshold);

        clij2.execute(MeanZProjectionAboveThreshold.class, "mean_z_projection_above_threshold_x.cl", "mean_z_projection_above_threshold", dst.getDimensions(), dst.getDimensions(), parameters);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number threshold";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the mean average intensity projection of an image along Z but only for pixels above a given threshold.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0};
    }
}
