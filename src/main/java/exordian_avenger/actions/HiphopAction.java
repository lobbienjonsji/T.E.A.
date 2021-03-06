package exordian_avenger.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import exordian_avenger.patches.CombatUpdatePatch;

import java.util.Iterator;


public class HiphopAction
        extends AbstractGameAction
{
    private AbstractPlayer p;
    private final boolean upgrade;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
    public static final String[] TEXT = uiStrings.TEXT;

    public HiphopAction(boolean upgrade)
    {
        this.upgrade = upgrade;
        this.p = AbstractDungeon.player;
        setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        Iterator<AbstractCard> d;
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (AbstractDungeon.player.hand.size() == 10)
            {
                AbstractDungeon.player.createHandIsFullDialog();
                this.isDone = true;
                return;
            }
            if (CombatUpdatePatch.recurrentPile.isEmpty())
            {
                this.isDone = true; return;
            }
            AbstractCard c;
            if (CombatUpdatePatch.recurrentPile.size() == 1)
            {
                c = CombatUpdatePatch.recurrentPile.getTopCard();
                c.unfadeOut();
                this.p.hand.addToHand(c);
                if ((AbstractDungeon.player.hasPower("Corruption")) && (c.type == AbstractCard.CardType.SKILL)) {
                    c.setCostForTurn(-9);
                }
                CombatUpdatePatch.recurrentPile.removeCard(c);
                if ((this.upgrade) && (c.canUpgrade())) {
                    c.upgrade();
                }
                c.unhover();
                c.fadingOut = false;
                this.isDone = true;
                CombatUpdatePatch.counter.clear();
                return;
            }
            for (AbstractCard e : CombatUpdatePatch.recurrentPile.group) {
                e.stopGlowing();
                e.unhover();
                e.unfadeOut();
            }
            if (CombatUpdatePatch.recurrentPile.isEmpty())
            {
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(CombatUpdatePatch.recurrentPile, 1, TEXT[0], false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                int numbertoremove = CombatUpdatePatch.recurrentPile.group.indexOf(c);
                CombatUpdatePatch.counter.remove(numbertoremove);
                this.p.hand.addToHand(c);
                if ((AbstractDungeon.player.hasPower("Corruption")) && (c.type == AbstractCard.CardType.SKILL)) {
                    c.setCostForTurn(-9);
                }
                CombatUpdatePatch.recurrentPile.removeCard(c);
                if ((this.upgrade) && (c.canUpgrade())) {
                    c.upgrade();
                }
                c.unhover();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.p.hand.refreshHandLayout();
            for (AbstractCard c : CombatUpdatePatch.recurrentPile.group)
            {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0F;
            }
        }
        tickDuration();
    }
}

