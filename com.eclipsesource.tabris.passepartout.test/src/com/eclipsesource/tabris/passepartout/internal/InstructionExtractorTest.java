/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.passepartout.internal;

import static com.eclipsesource.tabris.passepartout.PassePartout.maxWidth;
import static com.eclipsesource.tabris.passepartout.PassePartout.minWidth;
import static com.eclipsesource.tabris.passepartout.PassePartout.px;
import static com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil.createEnvironment;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.PassePartout;
import com.eclipsesource.tabris.passepartout.Rule;


public class InstructionExtractorTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEnvironment() {
    new InstructionExtractor( null );
  }

  @Test
  public void testPicksComplientRule() {
    InstructionExtractor extractor = new InstructionExtractor( createEnvironment( new Bounds( 0, 0, 100, 100 ) ) );
    Instruction instruction = PassePartout.columns( 5 );
    Rule rule = PassePartout.when( PassePartout.minWidth( PassePartout.px( 90 ) ) ).then( instruction );
    List<Rule> rules = new ArrayList<Rule>();
    rules.add( rule );

    List<Instruction> instructions = extractor.extract( rules );

    assertSame( instruction, instructions.get( 0 ) );
  }

  @Test
  public void testPicksComplientRuleWithMultipleRules() {
    InstructionExtractor extractor = new InstructionExtractor( createEnvironment( new Bounds( 0, 0, 100, 100 ) ) );
    Instruction instruction = PassePartout.columns( 5 );
    Rule rule = PassePartout.when( PassePartout.minWidth( PassePartout.px( 90 ) ) ).then( instruction );
    List<Rule> rules = new ArrayList<Rule>();
    rules.add( PassePartout.when( PassePartout.minWidth( PassePartout.px( 110 ) ) ).then( PassePartout.columns( 10 ) ) );
    rules.add( rule );

    List<Instruction> instructions = extractor.extract( rules );

    assertSame( instruction, instructions.get( 0 ) );
  }

  @Test
  public void testPicksComplientRuleWithMultipleConditionsAndWithMultipleRules() {
    InstructionExtractor extractor = new InstructionExtractor( createEnvironment( new Bounds( 0, 0, 100, 100 ) ) );
    Instruction instruction = PassePartout.columns( 5 );
    Rule rule = PassePartout.when( PassePartout.minWidth( PassePartout.px( 90 ) ) ).and( maxWidth( PassePartout.px( 110 ) ) ).then( instruction );
    List<Rule> rules = new ArrayList<Rule>();
    rules.add( PassePartout.when( PassePartout.minWidth( PassePartout.px( 110 ) ) ).then( PassePartout.columns( 10 ) ) );
    rules.add( rule );

    List<Instruction> instructions = extractor.extract( rules );

    assertSame( instruction, instructions.get( 0 ) );
  }

  @Test
  public void testPicksNoRuleWithMultipleOneNoComplientCondition() {
    InstructionExtractor extractor = new InstructionExtractor( createEnvironment( new Bounds( 0, 0, 100, 100 ) ) );
    Instruction instruction = PassePartout.columns( 5 );
    Rule rule = PassePartout.when( minWidth( px( 90 ) ) ).and( maxWidth( px( 99 ) ) ).then( instruction );
    List<Rule> rules = new ArrayList<Rule>();
    rules.add( rule );

    List<Instruction> instructions = extractor.extract( rules );

    assertTrue( instructions.isEmpty() );
  }

  @Test
  public void testCombinesComplientRulesWithMultipleRules() {
    InstructionExtractor extractor = new InstructionExtractor( createEnvironment( new Bounds( 0, 0, 100, 100 ) ) );
    List<Rule> rules = new ArrayList<Rule>();
    Instruction instruction = PassePartout.columns( 5 );
    Instruction instruction2 = PassePartout.columns( 10 );
    rules.add( PassePartout.when( minWidth( px( 90 ) ) ).then( instruction ) );
    rules.add( PassePartout.when( minWidth( px( 95 ) ) ).then( instruction2 ) );

    List<Instruction> instructions = extractor.extract( rules );

    assertSame( instruction, instructions.get( 0 ) );
    assertSame( instruction2, instructions.get( 1 ) );
  }

  @Test
  public void testReturnsEmptyInstructionsWithoutComplientRule() {
    InstructionExtractor extractor = new InstructionExtractor( createEnvironment( new Bounds( 0, 0, 80, 100 ) ) );
    List<Rule> rules = new ArrayList<Rule>();
    Instruction instruction = PassePartout.columns( 5 );
    Instruction instruction2 = PassePartout.columns( 10 );
    rules.add( PassePartout.when( minWidth( px( 90 ) ) ).then( instruction ) );
    rules.add( PassePartout.when( minWidth( px( 95 ) ) ).then( instruction2 ) );

    List<Instruction> instructions = extractor.extract( rules );

    assertTrue( instructions.isEmpty() );
  }

}
