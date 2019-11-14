package quaternary.zombievillagecontrol.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;

@SuppressWarnings("unused")
public class ClassTransformer implements IClassTransformer, Opcodes {
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(!transformedName.equals("net.minecraft.world.gen.structure.StructureVillagePieces$Start")) {
			return basicClass;
		}
		
		ClassReader reader = new ClassReader(basicClass);
		ClassNode node = new ClassNode(ASM5);
		reader.accept(node, 0);
		
		done:
		for(MethodNode method : node.methods) {
			//There's 2 constructors and I want the one with lots of parameters.
			if(!method.name.equals("<init>")) continue;
			if(method.desc.equals("()V")) continue;
			
			ListIterator<AbstractInsnNode> inserator = method.instructions.iterator();
			
			boolean found50 = false;
			
			for(AbstractInsnNode insn; inserator.hasNext();) {
				insn = inserator.next();
				
				if(!found50 && insn instanceof IntInsnNode && ((IntInsnNode) insn).operand == 50) {
					inserator.remove();
					
					inserator.add(new FieldInsnNode(
						GETSTATIC,
						"quaternary/zombievillagecontrol/Hooks",
						"zombieVillageChance",
						"I"
					));
					
					found50 = true;
				}
				
				//isZombieInfested happens to be the only boolean field to write to \_(:D)_/
				if(found50 && insn.getOpcode() == PUTFIELD) {
					//move before it
					inserator.previous();
					
					//add the flipping thing
					inserator.add(new FieldInsnNode(
						GETSTATIC,
						"quaternary/zombievillagecontrol/Hooks",
						"flipChance",
						"Z"
					));
					inserator.add(new InsnNode(IXOR));
					
					//All done
					break done;
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
		/*
		byte[] lol = writer.toByteArray();
		CheckClassAdapter.verify(new ClassReader(lol), null, true, new PrintWriter(System.err));
		return lol;
		*/
	}
}
