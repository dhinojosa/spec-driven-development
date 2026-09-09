package com.evolutionnext.jqwik.arbitraries;
import net.jqwik.api.*;

public class RecursionAndLazyTest {
    public sealed interface BinaryTree {
        record Node(int value, BinaryTree left, BinaryTree right) implements BinaryTree {
        }
        record Leaf(int value) implements BinaryTree {
        }
    }

    Arbitrary<BinaryTree> binaryTreeOfDepth(int depth) {
        if (depth <= 0) {
            return Arbitraries.integers().map(BinaryTree.Leaf::new);
        } else {
            return Arbitraries.oneOf(
                Arbitraries.integers().map(BinaryTree.Leaf::new),
                Combinators.combine(
                    Arbitraries.integers(),
                    binaryTreeOfDepth(depth - 1),
                    binaryTreeOfDepth(depth - 1)
                ).as(BinaryTree.Node::new)
            );
        }
    }

    @Provide
    Arbitrary<BinaryTree> binaryTreeOfDepth5() {
        return binaryTreeOfDepth(5);
    }

    @Provide
    Arbitrary<BinaryTree> binaryTreeOfDepthRandom() {
        return Arbitraries.integers().between(1, 10)
            .flatMap(this::binaryTreeOfDepth);
    }

    @Property(tries = 10)
    void testLazyTreesCanGenerateDeepStructuresOfASize(@ForAll("binaryTreeOfDepth5") BinaryTree tree) {
        System.out.printf("Tree: %s%n", tree);
    }

    @Property(tries = 10)
    void testLazyTreesCanGenerateDeepStructuresOfARandomSize(@ForAll("binaryTreeOfDepthRandom") BinaryTree tree) {
        System.out.printf("Tree: %s%n", tree);
    }
}
