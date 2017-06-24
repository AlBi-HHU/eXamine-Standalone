package org.cwi.examine.presentation.visualization.overview;

import com.sun.javafx.collections.ObservableSetWrapper;
import org.cwi.examine.model.HNode;
import org.cwi.examine.graphics.PVector;
import org.cwi.examine.graphics.StaticGraphics;
import org.cwi.examine.graphics.draw.Representation;
import org.cwi.examine.presentation.visualization.Parameters;
import org.cwi.examine.presentation.visualization.Visualization;
import org.cwi.examine.model.HAnnotation;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

// Link representation.
public class LinkRepresentation extends Representation<LinkRepresentation.Link> {
    private final Visualization visualization;
    public final DefaultEdge edge;  // Underlying edge.
    public final PVector[] cs;
    //public final LineString ls;     // Curve coordinates.

    public LinkRepresentation(final Visualization visualization,
            DefaultEdge edge,
                              HNode node1,
                              HNode node2,
                              PVector[] cs) {
        super(new Link(node1, node2));
        this.visualization = visualization;
        this.edge = edge;
        this.cs = cs;
        //this.ls = Util.circlePiece(cs[0], cs[1], cs[2], LINK_SEGMENTS);
    }

    @Override
    public PVector dimensions() {
        return PVector.v();
    }

    @Override
    public void draw() {
        boolean highlight = visualization.model.highlightedLinksProperty().get().contains(edge);
        
        StaticGraphics.picking();

        // Halo.
        double haloWeight = highlight ? Parameters.LINK_WIDTH + 4f : Parameters.LINK_WIDTH + 2f;
        StaticGraphics.color(org.cwi.examine.graphics.draw.Parameters.backgroundColor);
        StaticGraphics.strokeWeight(haloWeight);
        drawLink();
        
        // Actual edge.
        double edgeWeight = highlight ? Parameters.LINK_WIDTH + 2f : Parameters.LINK_WIDTH;
        StaticGraphics.color(highlight ? Color.BLACK: org.cwi.examine.graphics.draw.Parameters.textColor);
        StaticGraphics.strokeWeight(edgeWeight);
        drawLink();
    }
    
    private void drawLink() {
        StaticGraphics.circleArc(cs[0], cs[1], cs[2]);
        //drawLineString(ls);
        //drawLine(cs[0], cs[1]);
        //drawLine(cs[1], cs[2]);
    }

    @Override
    public void beginHovered() {
        // Highlight proteins term intersection.
        Set<HNode> hP = new HashSet<HNode>();
        hP.add(element.node1);
        hP.add(element.node2);
        visualization.model.highlightedNodesProperty().set(new ObservableSetWrapper<>(hP));
        
        // Highlight interactions.
        Set<DefaultEdge> hI = new HashSet<DefaultEdge>();
        hI.add(edge);
        visualization.model.highlightedLinksProperty().set(new ObservableSetWrapper<>(hI));
        
        // Intersect annotation annotations.
        Set<HAnnotation> hT = new HashSet<HAnnotation>();
        hT.addAll(element.node1.annotations);
        hT.retainAll(element.node2.annotations);
        visualization.model.highlightedAnnotations().set(new ObservableSetWrapper<>(hT));
    }

    @Override
    public void endHovered() {
        visualization.model.highlightedNodesProperty().clear();
        visualization.model.highlightedLinksProperty().clear();
        visualization.model.highlightedAnnotations().clear();
    }

    
    public static class Link {
        public HNode node1, node2;

        public Link(HNode node1, HNode node2) {
            this.node1 = node1;
            this.node2 = node2;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + (this.node1 != null ? this.node1.hashCode() : 0);
            hash = 29 * hash + (this.node2 != null ? this.node2.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Link other = (Link) obj;
            if (this.node1 != other.node1 && (this.node1 == null || !this.node1.equals(other.node1))) {
                return false;
            }
            if (this.node2 != other.node2 && (this.node2 == null || !this.node2.equals(other.node2))) {
                return false;
            }
            return true;
        }
    }
}
