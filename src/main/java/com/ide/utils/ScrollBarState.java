package com.ide.utils;


import com.ide.proyectos.TreeDirectorios;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;

public class ScrollBarState
{
    private final TreeDirectorios treeDirectorios;
    private final Orientation orientation;
    private ScrollBar scrollBar;

    private double min;
    private double max;
    private double value;
    private double blockIncrement;
    private double unitIncrement;

    // ---------------------------------------------------------------------------------//
    // constructor
    // ---------------------------------------------------------------------------------//

    public ScrollBarState (TreeDirectorios treeDirectorios, Orientation orientation)
    {
        this.treeDirectorios = treeDirectorios;
        this.orientation = orientation;
    }

    // ---------------------------------------------------------------------------------//
    // reset
    // ---------------------------------------------------------------------------------//

    public void reset ()
    {
        scrollBar = null;
    }

    // ---------------------------------------------------------------------------------//
    // save
    // ---------------------------------------------------------------------------------//

    public void save ()
    {
        if (scrollBar == null && !setScrollBar ())
            return;

        this.min = scrollBar.getMin ();
        this.max = scrollBar.getMax ();
        this.value = scrollBar.getValue ();
        this.blockIncrement = scrollBar.getBlockIncrement ();
        this.unitIncrement = scrollBar.getUnitIncrement ();

    }

    // ---------------------------------------------------------------------------------//
    // restore
    // ---------------------------------------------------------------------------------//

    public void restore ()
    {
        if (scrollBar == null)
            return;

        scrollBar.setMin (min);
        scrollBar.setMax (max);
        scrollBar.setValue (value);
        scrollBar.setUnitIncrement (unitIncrement);
        scrollBar.setBlockIncrement (blockIncrement);

    }

    // ---------------------------------------------------------------------------------//
    // setScrollBar
    // ---------------------------------------------------------------------------------//

    private boolean setScrollBar ()
    {
        for (Node node : treeDirectorios.lookupAll (".scroll-bar"))
            if (node instanceof ScrollBar
                    && ((ScrollBar) node).getOrientation ().equals (orientation))
            {
                scrollBar = (ScrollBar) node;
                return true;
            }
        return false;
    }
}